package sample.netty4.server.myProxyServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import sample.netty4.server.myProxyServer.entity.EventLoopGroups;
import sample.netty4.server.myProxyServer.handler.ProtocolHandler;

public class MyProxyServer implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MyProxyServer.class);

    private int serverPort;

    public MyProxyServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new MyProxyServer(port).afterPropertiesSet();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(EventLoopGroups.bossGroup, EventLoopGroups.workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            Tools.initHandler(ch)
                                    .addLast(new ProtocolHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(serverPort).sync();

            f.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("MyProxyServer Exception", e);
        } finally {
            EventLoopGroups.workerGroup.shutdownGracefully();
            EventLoopGroups.bossGroup.shutdownGracefully();
        }
    }
}
