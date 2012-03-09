package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.data._
import play.api.data.Forms._

import models.LoginData

object Login extends Controller {
	val loginForm: Form[LoginData] = Form(
	    mapping(
	        "url" -> text,
	        "username" -> text,
	        "password" -> text,
	        "remember_me" -> optional(boolean)
	    )
	    {
	    	(url, username, password, rememberMe) => LoginData(url, username, password, rememberMe)
	    }
	    {
	    	loginData => Some(loginData.url, loginData.username, loginData.password, loginData.rememberMe)
	    }
	)
	 
	    
	def index(url: String) = Action(
		Ok(views.html.login(url, loginForm))
	)
	
	def authenticate() = Action{ implicit request =>
	  	loginForm.bindFromRequest.fold(
	  	    errors => { println(errors)
	  	      BadRequest("Bod") },
	  	    loginData => MovedPermanently(loginData.url.replace(" ", "+")).withSession(
	  	          "user" -> loginData.username 
	  	    )
	  	)
	}
}