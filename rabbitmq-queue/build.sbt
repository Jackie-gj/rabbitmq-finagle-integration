// the Scala and AMQP Lift used is too old
// just to AMQP java client instead
// libraryDependencies += "net.liftweb" % "lift-amqp" % "2.0"

libraryDependencies += "com.rabbitmq" % "amqp-client" % "3.4.3"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.3.9"

libraryDependencies += "org.scala-lang" % "scala-actors" % "2.10.4"
