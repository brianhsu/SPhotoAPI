name := "SPhotoAPI"

version := "0.1"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scribe" % "scribe" % "1.3.5",
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
  "net.liftweb" %% "lift-json" % "2.5-RC5"
)

docDirectory in Compile <<= (baseDirectory / "api")

