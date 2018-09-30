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
  implicit val encoder: Encoder[Parameter] = deriveEncoder[Parameter].mapJsonObject(_.filter {
    case (_, Json.Null) => false
    case _              => true
  })

  case class Parameter(
      filter_by: Option[String],
      sort_by: Option[String],
      sort_order: Option[String],
      limit: Option[Int])

  implicit val myResponseDecoder: Decoder[MyResponse] = deriveDecoder[MyResponse]

  case class MyResponse(doc_ids: Seq[String], cursor: Cursor, has_more: Boolean)

  implicit val cursorDecoder: Decoder[Cursor] = deriveDecoder[Cursor]

  case class Cursor(value: String, expiration: String)

  // 型を指定して実行すると「polymorphic expression cannot be instantiated to expected type;」が発生する
  // おそらくこれ？ -> https://github.com/scala/bug/issues/8983
  val response =
    sttp
      .post(uri"https://api.dropboxapi.com/2/paper/docs/list")
      .auth
      .bearer(token)
      .contentType("application/json")
      .body(Parameter(filter_by = None, sort_by = None, sort_order = None, limit = None))
      .response(asJson[MyResponse])
      .send()
  response.body match {
    case Right(decoded) =>
      decoded match {
        case Right(myResponse) =>
          val cursorValue = myResponse.cursor.value
          case class ContinueParameter(cursor: String)
          implicit val encoder: Encoder[ContinueParameter] = deriveEncoder
          val continueResponse =
            sttp
              .post(uri"https://api.dropboxapi.com/2/paper/docs/list/continue")
              .auth
              .bearer(token)
              .contentType("application/json")
              .body(ContinueParameter(cursorValue))
              .send()
          println(continueResponse)
        case Left(_) =>
      }
    case Left(_) =>
  }
}
