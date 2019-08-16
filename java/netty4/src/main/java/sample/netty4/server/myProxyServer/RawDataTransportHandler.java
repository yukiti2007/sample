package sample.netty4.server.myProxyServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RawDataTransportHandler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        ByteBuf in =(ByteBuf)msg;
        in.markReaderIndex();
        String line = Tools.readLine(in);
        in.resetReaderIndex();


        String host="";

    }
}
