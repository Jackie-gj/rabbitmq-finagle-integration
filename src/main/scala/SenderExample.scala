import com.rabbitmq.client.ConnectionFactory
import java.io.{ByteArrayOutputStream, ObjectOutputStream}

class SenderExample {

  private val QUEUE_NAME = "fruits"

  // setup channel
  // ref https://github.com/rabbitmq/rabbitmq-tutorials/blob/master/java/Send.java
  val factory = new ConnectionFactory()
  factory.setHost("localhost")
  val conn = factory.newConnection()
  val channel = conn.createChannel()
  // durable = false, exclusive = false, autoDelete = false, arguments = null
  channel.queueDeclare(QUEUE_NAME, false, false, false, null)

  /**
   * Send 'Hello, world!'
   */
  def send = {
    val bytes = new ByteArrayOutputStream
    val store = new ObjectOutputStream(bytes)
    store.writeObject("Hello, world!")
    store.close
    channel.basicPublish("", QUEUE_NAME, null, bytes.toByteArray)
  }

}
