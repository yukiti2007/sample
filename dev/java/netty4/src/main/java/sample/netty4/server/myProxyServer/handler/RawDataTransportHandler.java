package sample.netty4.server.myProxyServer.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawDataTransportHandler extends BaseInBoundHandler {

    private static final Logger logger = LoggerFactory.getLogger(RawDataTransportHandler.class);

    private final Channel outChannel;

    public RawDataTransportHandler(Channel outChannel) {
        this.outChannel = outChannel;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.outChannel.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        this.outChannel.flush();
    }

//    @Override
//    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//        ctx.channel().close();
//        dstCtx.channel().close();
//    }

}
