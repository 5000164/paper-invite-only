package interfaces

import com.softwaremill.sttp._
import com.softwaremill.sttp.circe._
import domain.paper.{InviteOnlySharingPolicy, List, ListContinue}
import io.circe._
import io.circe.generic.semiauto._

/**
  * Dropbox Paper に関する処理を行う。
  *
  * レスポンスに関しては、
  * 型を指定して実行すると「polymorphic expression cannot be instantiated to expected type;」というエラーが発生するため
  * 一度変数に格納している。
  * よくわかっていないが、おそらく https://github.com/scala/bug/issues/8983 に関係するものだと思われる。
  *
  * @param token Dropbox Paper へアクセスする際に使用するトークン
  */
class Paper(val token: String) {
  implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()

  /**
    * ドキュメントの一覧を取得する。
    *
    * @return 取得した一覧
    */
  def list(
      filterBy: Option[String] = None,
      sortBy: Option[String] = None,
      sortOrder: Option[String] = None,
      limit: Option[Int] = None): Either[String, List.Response] = {
    implicit val encoder: Encoder[List.Parameter] = deriveEncoder[List.Parameter].mapJsonObject(_.filter {
      case (_, Json.Null) => false
      case _ => true
    })
    implicit val cursorDecoder: Decoder[List.Cursor] = deriveDecoder
    implicit val decoder: Decoder[List.Response] = deriveDecoder
    sttp
      .post(uri"https://api.dropboxapi.com/2/paper/docs/list")
      .auth
      .bearer(token)
      .contentType("application/json")
      .body(List.Parameter(filterBy, sortBy, sortOrder, limit))
      .response(asJson[List.Response])
      .send()
      .body match {
      case Right(response) =>
        response match {
          case Right(decoded) =>
            Right(decoded)
          case Left(e) =>
            Left(e.message)
        }
      case Left(e) =>
        Left(e)
    }
  }

  /**
    * ドキュメントの一覧の続きを取得する。
    *
    * @param cursor この値に指定した続きから取得する。
    * @return 取得した一覧
    */
  def listContinue(cursor: String): Either[String, ListContinue.Response] = {
    implicit val encoder: Encoder[ListContinue.Parameter] = deriveEncoder
    implicit val cursorDecoder: Decoder[ListContinue.Cursor] = deriveDecoder
    implicit val decoder: Decoder[ListContinue.Response] = deriveDecoder
    sttp
      .post(uri"https://api.dropboxapi.com/2/paper/docs/list/continue")
      .auth
      .bearer(token)
      .contentType("application/json")
      .body(ListContinue.Parameter(cursor))
      .response(asJson[ListContinue.Response])
      .send()
      .body match {
      case Right(response) =>
        response match {
          case Right(decoded) =>
            Right(decoded)
          case Left(e) =>
            Left(e.message)
        }
      case Left(e) =>
        Left(e)
    }
  }

  /**
    * ドキュメントの公開範囲を invite only にする
    *
    * @param id 処理対象のドキュメント
    * @return 実行結果
    */
  def inviteOnlySharingPolicy(id: String): Either[String, Unit] = {
    implicit val sharingPolicyEncoder: Encoder[InviteOnlySharingPolicy.SharingPolicy] = deriveEncoder
    implicit val encoder: Encoder[InviteOnlySharingPolicy.Parameter] = deriveEncoder
    sttp
      .post(uri"https://api.dropboxapi.com/2/paper/docs/sharing_policy/set")
      .auth
      .bearer(token)
      .contentType("application/json")
      .body(InviteOnlySharingPolicy.Parameter(id, InviteOnlySharingPolicy.SharingPolicy("invite_only")))
      .send()
      .body match {
      case Right(_) => Right(())
      case Left(e) => Left(e)
    }
  }
}
