package interfaces

import com.typesafe.config.ConfigFactory

object Application extends App {
  val conf = ConfigFactory.load()
  val token = conf.getString("token")
  val paper = new Paper(token)

  paper.list().body match {
    case Right(decoded) =>
      decoded match {
        case Right(response) =>
          val r = paper.listContinue(response.cursor.value)
          println(r)
        case Left(_) =>
      }
    case Left(_) =>
  }
}
