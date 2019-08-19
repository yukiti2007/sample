package sample.netty4.server.myProxyServer.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import sample.netty4.server.myProxyServer.Tools;
import sample.netty4.server.myProxyServer.entity.AttributeKeys;
import sample.netty4.server.myProxyServer.entity.Constants;
import sample.netty4.server.myProxyServer.enums.ProxyProtocol;
import sample.netty4.server.myProxyServer.enums.TransportProtocol;

public class ProtocolHandler extends BaseInBoundHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        // 标记读位置
        in.markReaderIndex();
        String line = Tools.readLine(in);
        in.resetReaderIndex();
        String protocol = line.split(" ")[0];

        if (Constants.HTTP_METHOD.contains(protocol)) {
            ctx.channel().attr(AttributeKeys.PROXY_PROTOCOL).set(ProxyProtocol.HTTP);
            ctx.channel().attr(AttributeKeys.TRANSPORT_PROTOCOL).set(TransportProtocol.HTTP);
            ctx.pipeline()
                    .addLast(new HttpServerCodec())
                    .addLast(new HttpObjectAggregator(65535))
                    .addLast(new HttpProxyAuthorizationHandler())
                    .addLast(new HttpRequestHeaderHandler())
                    .addLast(new ProxyHandler());

        } else if (Constants.HTTPS_METHOD.contains(protocol)) {
            ctx.channel().attr(AttributeKeys.PROXY_PROTOCOL).set(ProxyProtocol.HTTP);
            ctx.channel().attr(AttributeKeys.TRANSPORT_PROTOCOL).set(TransportProtocol.HTTPS);
            ctx.pipeline()
                    .addLast(new HttpServerCodec())
                    .addLast(new HttpObjectAggregator(65535))
                    .addLast(new HttpProxyAuthorizationHandler())
                    .addLast(new HttpRequestHeaderHandler())
                    .addLast(new ProxyHandler());
//
//        } else if (line.startsWith("5")) {
//            ctx.channel().attr(AttributeKeys.REQUEST_METHOD).set("SOCKS5");
//            //TODO
//        } else if (line.startsWith("4")) {
//            ctx.channel().attr(AttributeKeys.REQUEST_METHOD).set("SOCKS4");
//            //TODO
        }

        ctx.pipeline().remove(this);
        ctx.fireChannelRead(msg);
    }
}
