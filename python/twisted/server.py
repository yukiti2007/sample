# coding=utf-8
import datetime
import time

from twisted.internet import reactor
from twisted.internet.endpoints import TCP4ServerEndpoint
from twisted.internet.protocol import Factory
from twisted.internet.protocol import Protocol


class TcpServer(Protocol):
    CLIENT_MAP = {}

    def __init__(self, factory):
        self.connected = False
        self.factory = factory

    def connectionMade(self):
        self.connected = True
        self.factory.numProtocols = self.factory.numProtocols + 1
        addr = self.transport.client
        print("connected", self.transport.socket)
        TcpServer.CLIENT_MAP[addr] = self

    def connectionLost(self, reason):
        self.connected = False
        self.factory.numProtocols = self.factory.numProtocols - 1
        addr = self.transport.client
        if addr in TcpServer.CLIENT_MAP:
            print(addr, "Lost Connection from Tcp Server", 'Reason:', reason)
            del TcpServer.CLIENT_MAP[addr]

    def dataReceived(self, tcp_data):
        addr = self.transport.client
        nowTime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        try:
            msg = tcp_data.decode("utf-8")
            print("Received msg", msg, "from Tcp Client", addr)

            time.sleep(5)
            str = "来自服务器的响应 " + nowTime
            self.transport.write(str.encode("utf-8"))

        except BaseException as e:
            print("Comd Execute Error from", addr, "data:", tcp_data)
            str = "服务器发生异常 " + nowTime
            self.transport.write(str.encode("utf-8"))


class TcpServerFactory(Factory):
    def __init__(self):
        self.numProtocols = 0

    def buildProtocol(self, addr):
        return TcpServer(self)


# 启动tcp服务端
def start_tcp_server():
    port = 9527
    # endpoint = TCP4ServerEndpoint(reactor, port)
    # endpoint.listen(TcpServerFactory())
    reactor.listenTCP(port, TcpServerFactory())
    print("#####", "Starting TCP Server on", port, "#####")
    reactor.run()
    print("#####", "TCP Server is running on", port, "#####")


if __name__ == "__main__":
    start_tcp_server()
