package sample.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NettyServer implements InitializingBean, DisposableBean {

    private final int serverPort;

    private Channel ch;
    public static final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    public static final NioEventLoopGroup workerGroup = new NioEventLoopGroup(10);

    NettyServer(int port) {
        this.serverPort = port;
    }

    @Override
    public void destroy() throws Exception {
        try {
            System.out.println("准备关闭Netty服务器, 端口 " + serverPort);
            // 阻塞, 等待断开连接和关闭操作完成
            if (ch != null && ch.isActive()) {
                ch.close().awaitUninterruptibly();
            }
            if (!bossGroup.isShutdown()) {
                bossGroup.shutdownGracefully().awaitUninterruptibly();
            }
            if (!workerGroup.isShutdown()) {
                workerGroup.shutdownGracefully().awaitUninterruptibly();
            }
            System.out.println("成功关闭Netty服务器, 端口 " + serverPort);
        } catch (Exception e) {
            System.err.println("关闭Netty服务器时出错, 端口 " + serverPort + "\n" + e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        ch = b.bind(serverPort).await().channel();
    }
}
