package sample.netty4.server.myProxyServer.handler;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawDataTransportHandler extends BaseInBoundHandler {

    private static final Logger logger = LoggerFactory.getLogger(RawDataTransportHandler.class);

    private final ChannelHandlerContext dstCtx;

    public RawDataTransportHandler(ChannelHandlerContext dstCtx) {
        this.dstCtx = dstCtx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.dstCtx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        this.dstCtx.flush();
    }

//    @Override
//    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//        ctx.channel().close();
//        dstCtx.channel().close();
//    }

}
