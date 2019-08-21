package sample.netty4.server.myProxyServer.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import sample.netty4.server.myProxyServer.Tools;
import sample.netty4.server.myProxyServer.entity.AttributeKeys;
import sample.netty4.server.myProxyServer.entity.Constants;
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
            ctx.channel().attr(AttributeKeys.TRANSPORT_PROTOCOL).set(TransportProtocol.HTTP);
            ctx.channel().pipeline()
                    .addLast(new HttpServerCodec())
                    .addLast(new HttpObjectAggregator(65535))
                    .addLast(new HttpProxyAuthorizationHandler())
                    .addLast(new HttpRequestHeaderHandler())
                    .addLast(new Transport2HttpHandler());
            Tools.removeHandler(ctx.channel().pipeline(), this.getClass());

        } else if (Constants.HTTPS_METHOD.contains(protocol)) {
            ctx.channel().attr(AttributeKeys.TRANSPORT_PROTOCOL).set(TransportProtocol.HTTPS);
            ctx.channel().pipeline()
                    .addLast(new HttpServerCodec())
                    .addLast(new HttpObjectAggregator(65535))
                    .addLast(new HttpProxyAuthorizationHandler())
                    .addLast(new HttpRequestHeaderHandler())
                    .addLast(new Transport2HttpsHandler());
            Tools.removeHandler(ctx.channel().pipeline(), this.getClass());

        } else if (line.startsWith("5")) {
            // TODO
//            Tools.initHandler(ctx.channel())
//                    .addLast( Socks5ServerEncoder.DEFAULT)
//                    .addLast(new Socks5InitialRequestDecoder());
        } else if (line.startsWith("4")) {
            // TODO
//            Tools.initHandler(ctx.channel())
//                    .addLast(Socks4)
        }

        // TODO 继续转其他代理
//        Tools.removeHandler(ctx.channel().pipeline(), Transport2HttpHandler.class);
//        Tools.removeHandler(ctx.channel().pipeline(), Transport2HttpsHandler.class);
//        ctx.channel().pipeline()
//                .addLast(new Transport2ProxyHandler());

        ctx.fireChannelRead(msg);
    }
}
