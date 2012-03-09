package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.data._
import play.api.data.Forms._
import models.Hotel
import models.Booking

object Bookings extends Controller {
	val bookingForm: Form[Booking] = Form(
	    mapping(
	        "hotelId" -> longNumber,
	        "checkinDate" -> date("MM-dd-yyyy"),
	        "checkoutDate" -> date("MM-dd-yyyy"),
	        "creditCard" -> text,
	        "creditCardName" -> text,
	        "creditCardExpiryMonth" -> number,
	        "creditCardExpiryYear" -> number,
	        "smoking" -> text,
	        "beds" -> number,
	        "amenities" -> seq(text)
	        )
	    {
	    	(hotelId, checkinDate, checkoutDate, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear, smoking, beds, amenities) => 
	    	  Booking(null, null, hotelId, checkinDate, checkoutDate, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear, smoking, beds, amenities.mkString(","), "CREATED")
	    }
	    {
	        booking => Some(booking.hotelId, booking.checkinDate, booking.checkoutDate, booking.creditCard, booking.creditCardName, booking.creditCardExpiryMonth, booking.creditCardExpiryYear, booking.smoking, booking.beds, booking.amenities.split(","))
	    }
	    )
  
	def booking(hotelId: Long) = LoggedIn.AuthenticatedAction { (user, request) =>
		Ok(views.html.booking(Hotel.findById(hotelId), bookingForm))
	}
	
	def confirm() = LoggedIn.AuthenticatedAction { (user, request) =>
	  	bookingForm.bindFromRequest()(request).fold(
	  	    errors => BadRequest,
	  	    booking => {
	  	      val tostore = booking.copy(username = user)
	  	      val stored_booking = Booking.insert(tostore)
	  	      Ok(views.html.confirm(Hotel.findById(stored_booking.hotelId), stored_booking))
	  	    }
	  	)
	}
	
	def book() = LoggedIn.AuthenticatedAction { (user, request) => 
		request.body.asFormUrlEncoded.map { data => {
		  val booking = Booking.findById(data("bookingId").head.toLong)
		  if(data.contains("_eventId_confirm")) {
		    Booking.update(booking.copy(state = "BOOKED"))
		    MovedPermanently("/search?")
		  } else if(data.contains("_eventId_cancel")) {
		    Booking.update(booking.copy(state = "CANCELLED"))
		    MovedPermanently("/search?")
		  } else if(data.contains("_eventId_revise"))
	  	    Ok(views.html.confirm(Hotel.findById(booking.hotelId), booking))
		  else
		    BadRequest
		}
		}.getOrElse(BadRequest)
	}
}