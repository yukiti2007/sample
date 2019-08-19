package sample.netty4.server.myProxyServer.handler;

import com.google.common.base.Preconditions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;
import sample.netty4.server.myProxyServer.Tools;
import sample.netty4.server.myProxyServer.entity.AttributeKeys;
import sample.netty4.server.myProxyServer.entity.Constants;
import sample.netty4.server.myProxyServer.entity.HttpResponse;

public class ProxyHandler extends BaseInBoundHandler {

    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(10);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        doProxy(ctx, (HttpRequest) msg, 2);
    }

    private void doProxy(final ChannelHandlerContext ctxClient, HttpRequest request, final int retryCount) {
        Integer connectTimout = ctxClient.channel().attr(AttributeKeys.CONNECT_TIMOUT_MS).get();
        if (null == connectTimout) {
            connectTimout = Constants.CONNECT_TIMOUT_MS;
        }

        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimout)
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        Tools.initHandler(ch.pipeline())
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 1024 * 5))
                                .addLast(new ConnectResponseHandler(ctxClient));
                    }
                });

        String hostAdd = ctxClient.channel().attr(AttributeKeys.HOST_ADD).get();
        int hostPort = ctxClient.channel().attr(AttributeKeys.HOST_PORT).get();
        b.connect(hostAdd, hostPort).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    if (retryCount > 0) {
                        doProxy(ctxClient, request, retryCount - 1);
                    } else {
                        ReferenceCountUtil.release(request);
                        ctxClient.channel().writeAndFlush(HttpResponse.GATEWAY_TIMEOUT).addListener(ChannelFutureListener.CLOSE);
                    }
                    return;
                }
                future.channel().writeAndFlush(request);
            }
        });
    }


    private class ConnectResponseHandler extends BaseInBoundHandler {

        private ChannelHandlerContext ctxClient;

        public ConnectResponseHandler(ChannelHandlerContext ctxClient) {
            this.ctxClient = ctxClient;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctxServer, Object msg) throws Exception {
            Preconditions.checkArgument(msg instanceof FullHttpResponse);
            FullHttpResponse response = (FullHttpResponse) msg;

            ctxClient.channel().writeAndFlush(response).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    Tools.initHandler(ctxClient.pipeline())
                            .addLast(new RawDataTransportHandler(ctxServer));
                }
            });

            Tools.initHandler(ctxServer.pipeline())
                    .addLast(new RawDataTransportHandler(this.ctxClient));
        }
    }
}
