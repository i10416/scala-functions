scalaVersion := "2.13.1"

lazy val scalaFunctions = (project in file("."))
  .settings(
    name:="scalaFunctions",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.0" % "test",
      "com.google.cloud.functions" % "functions-framework-api" % "1.0.1",
    )
  )
