package sample.netty4.server.myProxyServer;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.netty4.server.myProxyServer.entity.AttributeKeys;
import sample.netty4.server.myProxyServer.entity.HttpResponse;

public class HttpProxyAuthorizationHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpProxyAuthorizationHandler.class);

//    public static HttpProxyAuthorizationHandler INSTANCE = new HttpProxyAuthorizationHandler();

    public HttpProxyAuthorizationHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String userName = ctx.channel().attr(AttributeKeys.USERNAME).get();
        String password = ctx.channel().attr(AttributeKeys.PASSWORD).get();
        try {
            boolean ok = true;

            if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
                ok = false;
                ctx.channel().writeAndFlush(new HttpResponse(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED)).addListener(ChannelFutureListener.CLOSE);
            } else {
                //TODO 验证用户名密码
                if (!"proxyUserName".equals(userName) || !"proxyPassword".equals(password)) {
                    ok = false;
                    ctx.channel().writeAndFlush(new HttpResponse(HttpResponseStatus.UNAUTHORIZED)).addListener(ChannelFutureListener.CLOSE);
                }
            }

            if (!ok) {
                System.out.println("HttpProxyAuthorizationHandler ERR ");
                System.out.println(msg.toString());
                ReferenceCountUtil.release(msg);
            } else {
                ctx.pipeline().remove(this);
                ctx.fireChannelRead(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
