import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1"
ThisBuild / scalaVersion := "2.12.12"
ThisBuild / autoCompilerPlugins := true
ThisBuild / scalacOptions ++= Seq(
  "-Xfatal-warnings", // for wartremover warts
  "-explaintypes", // Explain type errors in more detail.
  "-language:higherKinds" // Allow higher-kinded types
)

Global / onChangedBuildSource := ReloadOnSourceChanges


lazy val root = (project in file("."))
  .settings(
    name := "Trace4Cats101",
    mainClass in (Compile, run) := Some("Main"),
    mainClass in (Compile, packageBin) := Some("Main"),
    libraryDependencies ++= Seq(
      "io.janstenpickle" %% "trace4cats-core" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-inject" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-inject-zio" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-rate-sampling" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-fs2" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-http4s-client" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-http4s-server" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-sttp-client" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-natchez" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-avro-exporter" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-jaeger-thrift-exporter" % "0.7.0",
      "io.janstenpickle" %% "trace4cats-log-exporter" % "0.7.0"
    )

  )
