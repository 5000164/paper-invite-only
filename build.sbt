val typesafeConfig = "com.typesafe"     % "config"         % "1.3.3"
val circeCore = "io.circe"              %% "circe-core"    % "0.10.0"
val circeGeneric = "io.circe"           %% "circe-generic" % "0.10.0"
val circeParser = "io.circe"            %% "circe-parser"  % "0.10.0"
val sttpCore = "com.softwaremill.sttp"  %% "core"          % "1.3.5"
val sttpCirce = "com.softwaremill.sttp" %% "circe"         % "1.3.5"
val scalaTest = "org.scalatest"         %% "scalatest"     % "3.0.5" % "test"
val scalactic = "org.scalactic"         %% "scalactic"     % "3.0.5" % "test"
val scalaMock = "org.scalamock"         %% "scalamock"     % "4.1.0" % "test"

ThisBuild / organization := "jp.5000164"
ThisBuild / scalaVersion := "2.12.6"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "paper-invite-only",
    libraryDependencies ++= Seq(typesafeConfig, circeCore, circeGeneric, circeParser, sttpCore, sttpCirce, scalaTest, scalactic, scalaMock)
  )
