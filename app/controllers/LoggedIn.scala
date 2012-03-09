package controllers
import play.api.mvc.Action
import play.api.mvc.RequestHeader
import play.api.mvc.Results.MovedPermanently
import java.net.URLEncoder
import play.api.mvc.Result
import play.api.mvc.Request
import play.api.mvc.AnyContent

object LoggedIn {
	def AuthenticatedAction(action: (String, Request[AnyContent]) => Result) = {
	  Action { request =>
	    request.session.get("user").map { user =>
	      action(user, request)
	    }.getOrElse {
	      MovedPermanently("/login?url=" + URLEncoder.encode(request.uri, "UTF-8"))
	    }
	  }
	}
	
	def getUser(request: RequestHeader ): Option[String] = {
		request.session.get("user")
	}

    def Authenticated[A](action: Action[A]): Action[A] = {
	    Action(action.parser) { request => 
	        getUser(request).map(user => action(request)).getOrElse {
	            MovedPermanently("/login?url=" + URLEncoder.encode(request.uri, "UTF-8"))
	        }
	    }
    }
}
