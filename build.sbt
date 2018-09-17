
lazy val root = (project in file(".")).enablePlugins(PlayScala)

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.6"

name := """play-scala-starter-example"""

//crossScalaVersions := Seq("2.11.12", "2.12.6")
resolvers += Resolver.sonatypeRepo("snapshots")
//  addCompilerPlugin(
//    "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full
//  )

routesImport += "binders._"
libraryDependencies ++= Seq(
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.3.0",
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  "io.netty" % "netty-all" % "4.0.51.Final"
)

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.12.7",
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.13",
  "net.ruippeixotog" %% "scala-scraper" % "2.1.0"
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)