package interfaces

import com.typesafe.config.ConfigFactory
import domain.Documents
import domain.Documents.InviteOnlyWrapper

object Application extends App {
  val conf = ConfigFactory.load()
  val token = conf.getString("token")
  implicit val paper: Paper = new Paper(token)
  Documents.all.inviteOnly
}
