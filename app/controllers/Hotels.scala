package controllers

import play.api._
import play.api.mvc._

import views._

import models._

object Hotels extends Controller {
	    
	def search = Action { implicit request =>
	  LoggedIn.getUser(request).map { user =>
		Ok(views.html.search(Booking.findAllForUser(user))) 
	  }.getOrElse {
		Ok(views.html.search(Seq.empty)) 	    
	  }	
	}

	def result(searchString:String, pageSize:Int) = Action {
		Ok(views.html.hotels(Hotel.findAll(searchString)))
	}
	
	def detail(id:Long) = Action {
		Ok(views.html.detail(Hotel.findById(id)))
	}
}