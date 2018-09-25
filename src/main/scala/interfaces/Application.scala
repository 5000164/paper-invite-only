package interfaces

import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import com.typesafe.config.ConfigFactory
import io.circe._
import io.circe.generic.semiauto._

object Application extends App {
  val conf = ConfigFactory.load()
  val token = conf.getString("token")

  implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
  implicit val encoder: Encoder[Parameter] = deriveEncoder

  case class Parameter(filter_by: String, sort_by: String, sort_order: String, limit: Int)

  val response =
    sttp
      .post(uri"https://api.dropboxapi.com/2/paper/docs/list")
      .auth.bearer(token)
      .contentType("application/json")
      .body(Parameter("docs_created", "modified", "descending", 100))
      .send()
  println(response.body)
}
