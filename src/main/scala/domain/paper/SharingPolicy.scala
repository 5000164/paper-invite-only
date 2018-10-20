package domain.paper

object SharingPolicy {
  case class Parameter(doc_id: String)
  case class Response(public_sharing_policy: Tag, team_sharing_policy: Tag)
  case class Tag(`.tag`: String)
}
