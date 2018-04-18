import sbt.Keys._

organization := "de.riskident.meetup"
name := "ltk-props-testing"
version := "1.0"

lazy val ltkPropsTesting = project.in(file("."))
  .settings(
    scalaVersion := "2.12.4",
    scalacOptions += "-Ypartial-unification"
  )
  .settings(
    libraryDependencies ++=
      Seq(
        "org.typelevel" %% "cats-core" % "1.0.1",
        "org.typelevel" %% "cats-laws" % "1.0.1",
        "org.scalatest" %% "scalatest" % "3.0.5"
      )
  )

