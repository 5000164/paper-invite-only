package interfaces

import com.typesafe.config.ConfigFactory
import domain.Documents

object Application extends App {
  val conf = ConfigFactory.load()
  val token = conf.getString("token")
  implicit val paper: Paper = new Paper(token)
  val idList = Documents.all
  Documents.inviteOnly(idList)
}
