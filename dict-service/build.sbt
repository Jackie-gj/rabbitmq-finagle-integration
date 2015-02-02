com.twitter.scrooge.ScroogeSBT.newSettings

scroogeBuildOptions in Compile := Seq("--finagle", "--verbose")

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.apache.thrift" % "libthrift" % "0.8.0",
  "com.twitter" %% "scrooge-core" % "3.14.1",
  "com.twitter" %% "finagle-thrift" % "6.24.0",
  "com.rabbitmq" % "amqp-client" % "3.4.3",
  "com.typesafe.akka" % "akka-actor_2.10" % "2.3.9"
)