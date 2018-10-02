package domain.paper

object List {
  case class Parameter(
      filter_by: Option[String],
      sort_by: Option[String],
      sort_order: Option[String],
      limit: Option[Int])
  case class Response(doc_ids: Seq[String], cursor: Cursor, has_more: Boolean)
  case class Cursor(value: String, expiration: String)
}
