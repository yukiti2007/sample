package sample.netty4.server.myProxyServer.handler;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.netty4.server.myProxyServer.Tools;
import sample.netty4.server.myProxyServer.entity.AttributeKeys;
import sample.netty4.server.myProxyServer.entity.HttpResponse;

public class HttpProxyAuthorizationHandler extends BaseInBoundHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpProxyAuthorizationHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Preconditions.checkArgument(msg instanceof FullHttpRequest);
        FullHttpRequest request = (FullHttpRequest) msg;

        try {
            String userName = ctx.channel().attr(AttributeKeys.USERNAME).get();
            String password = ctx.channel().attr(AttributeKeys.PASSWORD).get();
            boolean ok = true;

            if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
                ok = false;
            } else {
                //TODO 验证用户名密码
                if (!"proxyUserName".equals(userName) || !"proxyPassword".equals(password)) {
                    ok = false;
                } else {
                    ctx.channel().attr(AttributeKeys.USERNAME).set(userName);
                }
            }

            if (!ok) {
                ctx.channel().writeAndFlush(HttpResponse.PROXY_AUTHENTICATION_REQUIRED);
            } else {
                Tools.removeHandler(ctx.pipeline(), this.getClass());
                ctx.fireChannelRead(msg);
            }

        } catch (Exception e) {
            ctx.channel().writeAndFlush(HttpResponse.PROXY_AUTHENTICATION_REQUIRED);
        }
    }
}
