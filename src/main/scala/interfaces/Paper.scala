package interfaces

import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import domain.paper.{List, ListContinue}
import io.circe._
import io.circe.generic.semiauto._

class Paper(val token: String) {
  implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()

  def list(): Response[Either[DeserializationError[Error], List.Response]] = {

    implicit val encoder: Encoder[List.Parameter] = deriveEncoder[List.Parameter].mapJsonObject(_.filter {
      case (_, Json.Null) => false
      case _              => true
    })
    implicit val cursorDecoder: Decoder[List.Cursor] = deriveDecoder[List.Cursor]
    implicit val decoder: Decoder[List.Response] = deriveDecoder[List.Response]

    // 型を指定して実行すると「polymorphic expression cannot be instantiated to expected type;」が発生する
    // おそらくこれ？ -> https://github.com/scala/bug/issues/8983
    // そのため一度変数に格納する
    val r = sttp
      .post(uri"https://api.dropboxapi.com/2/paper/docs/list")
      .auth
      .bearer(token)
      .contentType("application/json")
      .body(List.Parameter(filter_by = None, sort_by = None, sort_order = None, limit = Some(10)))
      .response(asJson[List.Response])
      .send()
    r
  }

  def listContinue(cursor: String) = {
    implicit val encoder: Encoder[ListContinue.Parameter] = deriveEncoder
    val r = sttp
      .post(uri"https://api.dropboxapi.com/2/paper/docs/list/continue")
      .auth
      .bearer(token)
      .contentType("application/json")
      .body(ListContinue.Parameter(cursor))
      .send()
    r
  }
}
