package interfaces

import com.typesafe.config.ConfigFactory
import domain.Documents
import domain.Documents.InviteOnlyWrapper

object Application extends App {
  val token = Option(System.console()) match {
    case Some(console) =>
      print("input token: ")
      console.readPassword().mkString
    case None => ConfigFactory.load().getString("token")
  }
  implicit val paper: Paper = new Paper(token)
  Documents.all.inviteOnly
}
