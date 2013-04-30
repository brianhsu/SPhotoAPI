name := "SPhotoAPI"

version := "0.1"

scalaVersion := "2.10.1"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scribe" % "scribe" % "1.3.3",
  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
)
