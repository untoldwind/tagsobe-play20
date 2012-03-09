package models

import play.api.db.DB
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Hotel(
    id: Pk[Long] = NotAssigned,
    name: String,
    price: Int,
    address: String,
    city: String,
    state: String,
    zip: String,
    country: String
    )

object Hotel {
  val simple = {
    get[Pk[Long]]("Hotel.id") ~
    get[String]("Hotel.name") ~
    get[Int]("Hotel.price") ~
    get[String]("Hotel.address") ~
    get[String]("Hotel.city") ~
    get[String]("Hotel.state") ~
    get[String]("Hotel.zip") ~
    get[String]("Hotel.country") map {
      case id~name~price~address~city~state~zip~country => 
        Hotel(id, name, price, address, city, state, zip, country)
    }
  }
  
  def findById(id: Long) : Hotel = 
    DB.withConnection(implicit connection => 
      SQL("select * from Hotel where id = {id}").on('id -> id).as(simple.single))
      
  def findAll(searchString:String) : Seq[Hotel] =
	DB.withConnection(implicit connection =>
	  SQL("select * from Hotel where name like {searchString}").on('searchString -> ("%" + searchString + "%")).as(simple *))
}