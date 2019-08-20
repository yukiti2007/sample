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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.ReferenceCountUtil;
import sample.netty4.server.myProxyServer.Tools;
import sample.netty4.server.myProxyServer.entity.AttributeKeys;
import sample.netty4.server.myProxyServer.entity.Constants;
import sample.netty4.server.myProxyServer.entity.HttpResponse;
import sample.netty4.server.myProxyServer.enums.TransportProtocol;

import javax.net.ssl.SSLException;

public class ProxyHandler extends BaseInBoundHandler {

    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(10);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        doProxy(ctx, (HttpRequest) msg, 2);
    }

    private void doProxy(final ChannelHandlerContext ctxClient, HttpRequest request, final int retryCount) throws SSLException {
        Integer connectTimout = ctxClient.channel().attr(AttributeKeys.CONNECT_TIMOUT_MS).get();
        if (null == connectTimout) {
            connectTimout = Constants.CONNECT_TIMOUT_MS;
        }
        SslContext sslCtx = null;
        String hostAdd = ctxClient.channel().attr(AttributeKeys.HOST_ADD).get();
        int hostPort = ctxClient.channel().attr(AttributeKeys.HOST_PORT).get();
        TransportProtocol tp = ctxClient.channel().attr(AttributeKeys.TRANSPORT_PROTOCOL).get();
        if (TransportProtocol.HTTPS == tp) {
            sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        }

        Bootstrap b = new Bootstrap();
        SslContext finalSslCtx = sslCtx;
        b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.AUTO_READ, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimout)
                .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        Tools.initHandler(ch, finalSslCtx)
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 1024 * 5));
                        ch.pipeline().addLast(new ConnectResponseHandler(ctxClient));
                    }
                });

        b.connect(hostAdd, hostPort).addListener(new ChannelFutureListener() {
//        b.connect("10.101.20.65", 9002).addListener(new ChannelFutureListener() {
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
                    Tools.initHandler(future.channel())
                            .addLast(new RawDataTransportHandler(ctxServer));
                }
            });

            SslContext sslCtx = null;
            TransportProtocol tp = ctxClient.channel().attr(AttributeKeys.TRANSPORT_PROTOCOL).get();
            if (TransportProtocol.HTTPS == tp) {
                sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            }
            Tools.initHandler(ctxServer.channel(), sslCtx)
                    .addLast(new RawDataTransportHandler(this.ctxClient));
        }
    }
}
