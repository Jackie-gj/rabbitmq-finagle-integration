package in.xnnyygn.dictservice.rabbitmq

import com.rabbitmq.client.ConnectionFactory

class DictServiceRabbitMqClient {

    val QUEUE_NAME = "DICT_SERVICE"

    val factory = new ConnectionFactory
    factory.setHost("localhost")
    val conn = factory.newConnection
    val channel = conn.createChannel
    // durable = false, exclusive = false, autoDelete = false, arguments = null
    channel.queueDeclare(QUEUE_NAME, false, false, false, null)

    def addWord(key: String, value: String): Unit = {
        channel.basicPublish("", QUEUE_NAME, null, (key + "=" + value).getBytes)
    }

}