name := "mandala-tsubuyaki"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.atilika.kuromoji" % "kuromoji-ipadic" % "0.9.0",
  "org.apache.spark" %% "spark-core" % "2.0.1"
)
