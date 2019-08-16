package sample.netty4.server.myProxyServer;

import com.google.common.base.Preconditions;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.ArrayList;

public class ProxyHandler extends ChannelInboundHandlerAdapter {

    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(10);

    ProxyHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    private void doProxy(final HttpRequest request, final ChannelHandlerContext ctxClient, final int connectTimoutMs, final int retryCount) {
new HttpObjectAggregator(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 20000)
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 0, 60));
                        pipeline.addLast(IdleCloseHandler.INSTANCE);
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 5));
                        pipeline.addLast(new ConnectResponseHandler(ctxClient));
                    }
                });
    }

    private class ConnectResponseHandler extends ChannelInboundHandlerAdapter {

        private ChannelHandlerContext previousCtx;

        public ConnectResponseHandler(ChannelHandlerContext ctxClient) {
            this.previousCtx = ctxClient;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Preconditions.checkArgument(msg instanceof HttpResponse);
            HttpResponse response = (HttpResponse) msg;
//            doOnConnectFinished(ctx, response, serverCtx);
        }
    }
}
