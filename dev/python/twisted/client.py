import datetime
import time

from twisted.internet import reactor
from twisted.internet.protocol import Protocol, ClientFactory


class TcpClient(Protocol):
    SERVER_MAP = {}  # 用于保存服务器端的连接信息

    def __init__(self):
        self.connected = False


    def connectionMade(self):
        self.connected = True
        addr = self.transport.addr  # 获取服务器端的连接信息
        print("connected", self.transport.socket)
        client_ip = addr[0]
        TcpClient.SERVER_MAP[client_ip] = self
        nowTime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        str = "你好服务器，我是客户端 " + nowTime
        self.transport.write(str.encode("utf-8"))  # 向服务器发送信息

    def connectionLost(self, reason):
        self.connected = False
        addr = self.transport.addr  # 获取服务器端的连接信息
        client_ip = addr[0]
        if client_ip in TcpClient.SERVER_MAP:
            del TcpClient.SERVER_MAP[client_ip]

    def dataReceived(self, tcp_data):
        addr = self.transport.addr  # 获取服务器端的连接信息
        nowTime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        try:
            msg = tcp_data.decode("utf-8")
            print("Received msg", msg, "from Tcp Server", addr)

            time.sleep(5)
            str = "来自客户端的请求 " + nowTime
            self.transport.write(str.encode("utf-8"))

        except BaseException as e:
            print("Comd Execute Error from", addr, "data:", tcp_data)
            str = "客户端发生异常 " + nowTime
            self.transport.write(str.encode("utf-8"))

class TcpClientFactory(ClientFactory):
    def __init__(self):
        self.protocol = None

    def startedConnecting(self, connector):
        print("Starting Connecting To Tcp Server", (connector.host, connector.port))

    def buildProtocol(self, addr):
        print("Connected To Tcp Server", addr)
        self.protocol = TcpClient()
        return self.protocol

    def clientConnectionLost(self, connector, reason):
        print("Lost Connection from Tcp Server", (connector.host, connector.port), 'Reason:', reason)
        time.sleep(30)
        connector.connect()

    def clientConnectionFailed(self, connector, reason):
        print("Failed To Connect To Tcp Server", (connector.host, connector.port), 'Reason:', reason)
        time.sleep(30)
        connector.connect()


# 连接到服务器
def start_tcp_client():
    host = "127.0.0.1"
    port = 9527
    port = 8080
    reactor.connectTCP(host, port, TcpClientFactory())
    reactor.run()


if __name__ == "__main__":
    start_tcp_client()
