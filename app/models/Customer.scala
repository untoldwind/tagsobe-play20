package models
import anorm.Pk
import anorm.NotAssigned

case class Customer(
    id: Pk[Long] = NotAssigned,
    name:String,
    username: String,
    password: String)

object Customer {
  
}