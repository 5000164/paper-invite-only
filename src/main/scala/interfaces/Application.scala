package interfaces

import com.typesafe.config.ConfigFactory
import domain.documents

object Application extends App {
  val conf = ConfigFactory.load()
  val token = conf.getString("token")
  val paper = new Paper(token)

  val idList = documents.all(paper)
  println(idList)
}
