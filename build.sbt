name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.mongodb" % "mongo-java-driver" % "3.2.2",
  "org.springframework.data" % "spring-data-mongodb" % "1.8.4.RELEASE",
  "me.prettyprint" % "hector-core" % "1.0-5",
  "com.mortennobel" % "java-image-scaling" % "0.8.6",
  "org.json" % "json" % "20151123"

)

resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

fork in run := true