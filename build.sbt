name := "fp-practice"

version := "0.1"

scalaVersion := "2.12.4"
testOptions in Test += Tests.Argument("-P8")


libraryDependencies ++= Seq(
  "org.typelevel"   %% "cats-core"  % "2.1.1",
  "com.typesafe"    %  "config"     % "1.4.0",
  "org.scalatest"   %% "scalatest"  % "3.1.2",
  "io.monix"        %% "monix"      % "3.3.0",
  "io.estatico" %% "newtype" % "0.4.4"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
