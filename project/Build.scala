import sbt._
import Keys._


object ApplicationBuild extends Build {
	val appName = "vsp"
	val appVersion = "0.2.0"
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
			libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test",
			libraryDependencies <+= scalaVersion {
				"org.scala-lang" % "scala-actors" % _
			}
		))
}
