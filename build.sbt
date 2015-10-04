name := "dogstar"
version := "1.0"
scalaVersion := "2.11.7"
lazy val root = (project in file(".")).enablePlugins(PlayScala)
fork in run := true

libraryDependencies ++= Seq(
	cache,
	"org.scala-lang.modules" %% "scala-xml" % "1.0.5"	
	)

routesGenerator := InjectedRoutesGenerator


fork in run := true