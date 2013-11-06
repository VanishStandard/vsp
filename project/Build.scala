import sbt._
import Keys._


object ApplicationBuild extends Build {
	val appName = "vsp"
	val appVersion = "0.5.0"
	val appOrganization = "com.v_standard.vsp"
	val buildScalaVersion = "2.10.0"

	lazy val root = Project(id = appName,
		base = file("."),
		settings = Project.defaultSettings ++ Seq(
			name := appName,
			organization := appOrganization,
			version := appVersion,
			scalaVersion := buildScalaVersion,
			publishMavenStyle := true,
			otherResolvers := Seq(Resolver.file("dotM2", file(Path.userHome + "/.m2/repository"))),
			publishLocalConfiguration <<= (packagedArtifacts, deliverLocal, ivyLoggingLevel) map {
				(arts, _, level) => new PublishConfiguration(None, "dotM2", arts, List[String](), level)
			},
			unmanagedBase in Test <<= baseDirectory { base => base / "src/test/lib" },
			testOptions += Tests.Setup(cl =>
				cl.loadClass("org.slf4j.LoggerFactory").
					getMethod("getLogger",cl.loadClass("java.lang.String")).
					invoke(null,"ROOT")
			),
			resolvers ++= Seq(
				Resolver.mavenLocal,
				"VanishStandard Maven Repository" at "http://vanishstandard.github.com/mvn-repo"
			),
			libraryDependencies ++= Seq(
				"com.typesafe" %% "scalalogging-slf4j" % "1.0.1",
				"org.slf4j" % "slf4j-api" % "1.7.2",
				"ch.qos.logback" % "logback-classic" % "1.0.9",
				"com.v_standard.utils" %% "utils" % "0.1.0",
				"org.scalatest" %% "scalatest" % "1.9.1" % "test"
			),
			libraryDependencies <+= scalaVersion {
				"org.scala-lang" % "scala-actors" % _
			}
		))
}
