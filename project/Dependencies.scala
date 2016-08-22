import sbt._

object Version {
  final val Akka      = "2.4.9"
  final val Scala     = "2.11.8"
  final val ScalaTest = "3.0.0"
}

object Library {
  val akkaHttpExperimental = "com.typesafe.akka" %% "akka-http-experimental" % Version.Akka
  val scalaTest            = "org.scalatest"     %% "scalatest"              % Version.ScalaTest
}
