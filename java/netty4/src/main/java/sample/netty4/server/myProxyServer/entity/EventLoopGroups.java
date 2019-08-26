package sample.netty4.server.myProxyServer.entity;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class EventLoopGroups {
    public static final EventLoopGroup bossGroup = new NioEventLoopGroup();
    public static final EventLoopGroup workerGroup = new NioEventLoopGroup();
}
