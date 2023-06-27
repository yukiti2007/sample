# coding=utf-8
import datetime
import time

from twisted.internet import reactor
from twisted.internet.protocol import ServerFactory, Factory
from twisted.internet.protocol import Protocol


class TcpServer(Protocol):
    CLIENT_MAP = {}  # 用于保存客户端的连接信息

    def __init__(self):
        self.connected = False

    def connectionMade(self):
        self.connected = True
        addr = self.transport.client  # 获取客户端的连接信息
        print("connected", self.transport.socket)
        TcpServer.CLIENT_MAP[addr] = self

    def connectionLost(self, reason):
        self.connected = False
        addr = self.transport.client  # 获取客户端的连接信息
        if addr in TcpServer.CLIENT_MAP:
            print(addr, "Lost Connection from Tcp Server", 'Reason:', reason)
            del TcpServer.CLIENT_MAP[addr]

    def dataReceived(self, tcp_data):
        addr = self.transport.client  # 获取客户端的连接信息
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


# 启动tcp服务端
def start_tcp_server():
    port = 9527
    serverFactory = Factory.forProtocol(TcpServer)
    reactor.listenTCP(port, serverFactory)
    print("#####", "Starting TCP Server on", port, "#####")
    reactor.run()
    print("#####", "TCP Server is running on", port, "#####")


if __name__ == "__main__":
    start_tcp_server()
