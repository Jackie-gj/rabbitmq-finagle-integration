package in.xnnyygn.dictservice

import com.twitter.util.{Await, Future}
import com.twitter.finagle.Thrift

class DictServiceTestServer {

    def start = {
        val service = Thrift.serveIface("localhost:9999", new DictServiceImpl)
        Await.ready(service)
    }
    
}