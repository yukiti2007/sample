package sample.netty4.server.myProxyServer.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.netty4.server.myProxyServer.entity.HttpResponse;

public class BaseInBoundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpProxyAuthorizationHandler.class);

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, Throwable cause) {
        ctx.writeAndFlush(HttpResponse.BAD_GATEWAY).addListener(ChannelFutureListener.CLOSE);
        logger.error("BaseInBoundHandler ", cause);
    }
}
