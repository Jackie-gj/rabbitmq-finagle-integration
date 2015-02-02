package in.xnnyygn.dictservice.rabbitmq

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.rabbitmq.client.{ConnectionFactory, Connection, Channel, QueueingConsumer}
import com.twitter.finagle.Thrift
import in.xnnyygn.dictservice.DictService

class DictServiceRabbitMqServer {

    val QUEUE_NAME = "DICT_SERVICE"

    def receive(actor: ActorRef): Unit = {
        val factory = new ConnectionFactory
        factory.setHost("localhost")
        val connection = factory.newConnection
        val channel = connection.createChannel
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        val consumer = new QueueingConsumer(channel)
        channel.basicConsume(QUEUE_NAME, true, consumer)
        while (true) {
            val delivery = consumer.nextDelivery
            DictServiceRequest(delivery.getBody) match {
                case Some(r) => actor ! r
                case _ => "ignore incorrect request"
            }
        }
    }

}

sealed abstract class DictServiceRequest

case class DictServicePutRequest(key: String, value: String) extends DictServiceRequest

object DictServiceRequest {
    def apply(bytes: Array[Byte]): Option[DictServiceRequest] = {
        val string = new String(bytes)
        println("got request " + string)
        val index = string.indexOf('=')
        if(index < 0) None
        else Some(DictServicePutRequest(string.substring(0, index), string.substring(index + 1)))
    }
}

class DictServiceRabbitMqServerActor extends Actor {

    val dictService = Thrift.newIface[DictService.FutureIface]("localhost:9999")

    def receive = {
        case r@DictServicePutRequest(key, value) => {
            println("process request " + r)
            dictService.put(key, value).onSuccess(_ =>
                println(s"put $key = $value")
            ).onFailure(println)
        }
    }

}

object DictServiceRabbitMqServer {

    def main(args: Array[String]): Unit = {
        val server = new DictServiceRabbitMqServer
        val system = ActorSystem("dictService")
        val actor = system.actorOf(Props[DictServiceRabbitMqServerActor], name = "dictServiceServer")
        server.receive(actor)
    }

}