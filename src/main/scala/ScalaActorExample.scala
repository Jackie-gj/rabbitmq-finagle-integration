import java.net.{InetAddress, UnknownHostException}
import scala.actors.Actor

object NameResovler extends Actor {

  type ErrorMessage = String
  type IpAddress = String

  case class LookupIpRequest(hostname: String, actor: Actor)
  case class LookupIpResult(hostname: String, ip: Either[ErrorMessage, IpAddress])

  def act = loop {
    react {
      case LookupIpRequest(hostname, actor) => actor ! LookupIpResult(hostname, getIpAddress(hostname))
    }
  }

  def getIpAddress(hostname: String): Either[ErrorMessage, IpAddress] = try {
    Right(InetAddress.getByName(hostname).getHostAddress)
  } catch {
    case _: UnknownHostException => Left(s"unknown host [$hostname]")
  }

}

object NameResolverExample extends Actor {

  import NameResovler.{LookupIpRequest, LookupIpResult}

  def act = loop {
    react {
      case LookupIpResult(hostname, Left(errorMessage)) => println(s"failed to lookup [$hostname], cause [$errorMessage]")
      case LookupIpResult(hostname, Right(ipAddress)) => println(s"[$hostname] => [$ipAddress]")
    }
  }

  def main(args: Array[String]): Unit = {
    NameResovler.start
    NameResolverExample.start

    NameResovler ! LookupIpRequest("xnnyygn.in", this)
    println("sent request 1")
    NameResovler ! LookupIpRequest("xnnyygn.in", this)
    println("sent request 2")
    NameResovler ! LookupIpRequest("no.such.host", this)
    println("sent request 3")
  }

}
