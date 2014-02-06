name := "SPhotoAPI"

version := "0.4"

scalaVersion := "2.10.3"

organization := "org.bone"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= Seq(
  "org.scribe" % "scribe" % "1.3.5",
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
  "net.liftweb" %% "lift-json" % "2.6-M2",
  "commons-codec" % "commons-codec" % "1.9",
  "commons-io" % "commons-io" % "2.4",
  "org.apache.httpcomponents" % "httpclient" % "4.3.2",
  "org.apache.httpcomponents" % "httpmime" % "4.3.2"
)

target in Compile in doc <<= baseDirectory(_ / "api")

publishTo := Some(
  Resolver.sftp("bone", "bone.twbbs.org.tw", "public_html/ivy") as ("linuxhsu")
)

