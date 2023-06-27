package sample.netty4.server.myProxyServer.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class IdleCloseHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(IdleCloseHandler.class);

    public static IdleCloseHandler INSTANCE = new IdleCloseHandler();

    private IdleCloseHandler() {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            logger.info("Channel [{}] Timeout", ctx.channel());
            ctx.channel().close();
        }
        super.userEventTriggered(ctx, evt);
    }
}

