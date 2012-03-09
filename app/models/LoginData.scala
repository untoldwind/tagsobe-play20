package models

case class LoginData (
    url: String,
	username: String,
	password: String,
	rememberMe: Option[Boolean]
)