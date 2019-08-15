package sample.netty4.server.myProxyServer;

import com.google.common.collect.ImmutableSet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class ProtocolHandler extends ChannelInboundHandlerAdapter {


    private static final ImmutableSet HTTP_METHOD = (new ImmutableSet.Builder())
            .add("HEAD")
            .add("GET")
            .add("POST")
            .add("PUT")
            .add("DELETE")
            .build();

    private static final ImmutableSet HTTPS_METHOD = (new ImmutableSet.Builder())
            .add("CONNECT")
            .build();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        // 标记读位置
        in.markReaderIndex();
        String line = Tools.readLine(in);
        String protocol = line.split(" ")[0];

        if (HTTP_METHOD.contains(protocol)) {
            ctx.pipeline()

                    // ChannelInboundHandlerAdapter
                    .addLast(HttpProxyAuthorizationHander.INSTANCE)
                    .addLast(new HttpRequestDecoder(4096, 64 * 1024, 128 * 1024))
                    .addLast(new HttpObjectAggregator(1024 * 1024 * 5))

                    //ChannelOutboundHandlerAdapter
                    .addLast(new HttpResponseEncoder());

        } else if (HTTPS_METHOD.contains(protocol)) {
            ctx.pipeline()

                    // ChannelInboundHandlerAdapter
                    .addLast(HttpProxyAuthorizationHander.INSTANCE)
                    .addLast(new HttpRequestDecoder(4096, 64 * 1024, 128 * 1024))
                    .addLast(new HttpObjectAggregator(1024 * 1024 * 5))

                    //ChannelOutboundHandlerAdapter
                    .addLast(new HttpResponseEncoder());

        } else {
            //TODO
        }
        System.out.println("############# " + protocol);
//        while (!("\r\n".equals(line) || "\n".equals(line))) {
//            line = Tools.readLine(in);
//            System.out.println("#############\n" + line);
//        }
        in.resetReaderIndex();
        ctx.pipeline().remove(this.getClass());
        ctx.fireChannelRead(msg);
    }
}
