package sample.netty4.server.myProxyServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpClientCodec;
import sample.netty4.server.myProxyServer.entity.AttributeKeys;
import sample.netty4.server.myProxyServer.entity.Constants;

public class ProtocolHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        // 标记读位置
        in.markReaderIndex();
        int length = in.readableBytes();
        byte[] content = new byte[length];
        in.readBytes(content);
        System.out.println("######################### \n"
                + new String(content));
        in.resetReaderIndex();

        String line = Tools.readLine(in);
        in.resetReaderIndex();
        String protocol = line.split(" ")[0];

//        if (Constants.HTTP_METHOD.contains(protocol)) {
            ctx.channel().attr(AttributeKeys.REQUEST_METHOD).set("HTTP");
            ctx.pipeline()

                    // ChannelInboundHandlerAdapter
                    .addLast(new HttpClientCodec())
                    .addLast(HttpProxyAuthorizationHandler.INSTANCE)
                    .addLast(new RequestHeaderDecoder())
//                    .addLast(new HttpRequestDecoder(4096, 64 * 1024, 128 * 1024))
//                    .addLast(new HttpObjectAggregator(1024 * 1024 * 5))
                    .addLast(new PrintHandler())

            //ChannelOutboundHandlerAdapter
//                    .addLast(new HttpResponseEncoder())
            ;

//        } else if (Constants.HTTPS_METHOD.contains(protocol)) {
//            ctx.channel().attr(AttributeKeys.REQUEST_METHOD).set("HTTPS");
//            ctx.pipeline()
//
//                    // ChannelInboundHandlerAdapter
//                    .addLast(new HttpClientCodec())
//                    .addLast(HttpProxyAuthorizationHandler.INSTANCE)
//                    .addLast(new RequestHeaderDecoder())
////                    .addLast(new HttpRequestDecoder(4096, 64 * 1024, 128 * 1024))
////                    .addLast(new HttpObjectAggregator(1024 * 1024 * 5))
//
//            //ChannelOutboundHandlerAdapter
////                    .addLast(new HttpResponseEncoder())
//            ;
//
//        } else if (line.startsWith("5")) {
//            ctx.channel().attr(AttributeKeys.REQUEST_METHOD).set("SOCKS5");
//            //TODO
//        } else if (line.startsWith("4")) {
//            ctx.channel().attr(AttributeKeys.REQUEST_METHOD).set("SOCKS4");
//            //TODO
//        }

        ctx.pipeline().remove(this.getClass());
        ctx.fireChannelRead(msg);
    }
}
