ThisBuild / scalaVersion := "2.12.6"

lazy val paperInviteOnly = (project in file("."))
  .settings(
    name := "paper-invite-only"
  )
