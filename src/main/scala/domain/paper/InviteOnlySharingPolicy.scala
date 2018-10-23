package domain.paper

object InviteOnlySharingPolicy {
  case class Parameter(doc_id: String, sharing_policy: SharingPolicy)
  case class SharingPolicy(public_sharing_policy: String)
}
