import Dependencies._

val versionBuild = "1.0"
val scalaVersionBuild = "2.11.1"
val organizationName ="org.geoblink"


lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    inThisBuild(List(
      organization := organizationName,
      scalaVersion := scalaVersionBuild,
      version := versionBuild
    )),
    name := "GeoblinkTest",
    mainClass in assembly := Some(""),
    assemblyJarName in assembly := s"geoblinkTest.jar",
    libraryDependencies ++= Seq(
      scalaTest % "it, test",
      scalaTest % "it, test",
      "org.apache.spark" %% "spark-core" % "2.2.0",
      "org.apache.spark" %% "spark-sql" % "2.2.0"
    ))

scalacOptions += "-feature"

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("reference.conf") => MergeStrategy.first
  case _ => MergeStrategy.first
}


