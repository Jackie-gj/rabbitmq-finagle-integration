package in.xnnyygn.dictservice

import com.twitter.finagle.Thrift

class DictServiceTestClient {
    val stub = Thrift.newIface[DictService.FutureIface]("localhost:9999")
}