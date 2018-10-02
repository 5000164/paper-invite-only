package domain.paper

object ListContinue {
  case class Parameter(cursor: String)
  case class Response(doc_ids: Seq[String], cursor: Cursor, has_more: Boolean)
  case class Cursor(value: String, expiration: String)
}
