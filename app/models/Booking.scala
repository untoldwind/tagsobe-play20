package models

import java.util.Date

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB

case class Booking (
    id: Pk[Long] = NotAssigned,
    username: String,
    hotelId: Long,
    checkinDate: Date,
    checkoutDate: Date,
    creditCard: String,
    creditCardName: String,
    creditCardExpiryMonth: Int,
    creditCardExpiryYear: Int,
    smoking: String,
    beds: Int,
    amenities: String,
    state: String
)

object Booking {
  val simple = {
    get[Pk[Long]]("Booking.id") ~
    get[String]("Booking.username") ~
    get[Long]("Booking.hotel") ~
    get[Date]("Booking.checkinDate") ~
    get[Date]("Booking.checkoutDate") ~
    get[String]("Booking.creditCard") ~
    get[String]("Booking.creditCardName") ~
    get[Int]("Booking.creditCardExpiryMonth") ~
    get[Int]("Booking.creditCardExpiryYear") ~
    get[String]("Booking.smoking") ~
    get[Int]("Booking.beds") ~
    get[String]("Booking.amenities") ~
    get[String]("Booking.state") map {
      case id~username~hotelId~checkinDate~checkoutDate~creditCard~creditCardName~creditCardExpiryMonth~creditCardExpiryYear~smoking~beds~amenities~state => 
        Booking(id, username, hotelId, checkinDate, checkoutDate, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear, smoking, beds, amenities, state)
    }
  }
  
  def findById(id: Long) : Booking = 
    DB.withConnection(implicit connection => 
      SQL("select * from Booking where id = {id}").on('id -> id).as(simple.single))
      
  def findAllForUser(username: String) : Seq[Booking] = 
	DB.withConnection(implicit connection =>
	  SQL("select * from Booking where username = {username}").on('username -> username).as(simple *))
    
  def insert(booking: Booking) : Booking = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into Booking (username, hotel, checkinDate, checkoutDate, creditCard, creditCardName, creditCardExpiryMonth, creditCardExpiryYear, smoking, beds, amenities, state) 
          values ( {username}, {hotelId}, {checkinDate}, {checkoutDate}, {creditCard}, {creditCardName}, {creditCardExpiryMonth}, {creditCardExpiryYear}, {smoking}, {beds}, {amenities}, {state} )
        """
      ).on(
        'username -> booking.username,
        'hotelId -> booking.hotelId,
        'checkinDate -> booking.checkinDate,
        'checkoutDate -> booking.checkoutDate,
        'creditCard -> booking.creditCard,
        'creditCardName -> booking.creditCardName,
        'creditCardExpiryMonth -> booking.creditCardExpiryMonth,
        'creditCardExpiryYear -> booking.creditCardExpiryYear,
        'smoking -> booking.smoking,
        'beds -> booking.beds,
        'amenities -> booking.amenities,
        'state -> booking.state
      ).executeUpdate()
      val id = SQL("select LAST_INSERT_ID()").as(scalar[Long].single)
      booking.copy(id = Id(id))
    }
  }
 
  def update(booking : Booking) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          update Booking set state={state} where id={id}
        """
      ).on(
        'id -> booking.id,
        'state -> booking.state
      ).executeUpdate()
    }
  }
}