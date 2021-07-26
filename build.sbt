name := "build_systems_a_la_carte"

version := "0.2"

scalaVersion := "3.0.1"

scalacOptions ++= Seq("-deprecation")

libraryDependencies ++= Seq(
 // Forced to use an old cats-core version, most likely
 // because of compatibility problems with graphs-cats
 ("org.typelevel" %% "cats-core" % "2.1.1").cross(CrossVersion.for3Use2_13),
 "org.typelevel" %% "cats-effect" % "3.1.1",
 "org.typelevel" %% "cats-mtl" % "1.2.1",
 ("com.flowtick" %% "graphs-core" % "0.5.0").cross(CrossVersion.for3Use2_13),
 ("com.flowtick" %% "graphs-cats" % "0.5.0").cross(CrossVersion.for3Use2_13),
 "org.scalameta" %% "munit" % "0.7.26" % Test,
 "org.scalacheck" %% "scalacheck" % "1.15.4" % Test
)
