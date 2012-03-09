package controllers

import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Results.MovedPermanently

object Application extends Controller {  
  def index = Action {
    MovedPermanently("/search")
  }
}