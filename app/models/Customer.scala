package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db.DB

case class Customer(
    id: Pk[Long] = NotAssigned,
    name:String,
    username: String,
    password: Option[String])

object Customer {
  val simple = {
    get[Pk[Long]]("Customer.id") ~
    get[String]("Customer.name") ~
    get[String]("Customer.username") ~
    get[Option[String]]("Customer.password") map {
      case id~name~username~password => 
        Customer(id, name, username, password)
    }
  }

  def findById(id: Long) : Customer = 
    DB.withConnection(implicit connection => 
      SQL("select * from Customer where id = {id}").on('id -> id).as(simple.single))

  def findByUsername(username: String) : Option[Customer] = 
    DB.withConnection(implicit connection => 
      SQL("select * from Customer where username = {username}").on('username -> username).as(simple.singleOpt))

}