import play.sbt.routes

name := "scala_elasticsearch"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq( jdbc , cache , ws,
  "joda-time" % "joda-time" % "2.8.1",
  "com.github.tototoshi" %% "scala-csv" % "1.2.1",
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "1.6.2",
  "com.kenshoo" %% "metrics-play" % "2.4.0_0.3.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % Test,
  "org.scalatestplus" %% "play" % "1.2.0" % Test
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

val appDependencies = Seq(
"com.kenshoo" %% "metrics-play" % "2.4.0_0.3.0"
)