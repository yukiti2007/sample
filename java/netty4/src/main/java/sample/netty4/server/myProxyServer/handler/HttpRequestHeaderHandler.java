package sample.netty4.server.myProxyServer.handler;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import com.google.common.net.HttpHeaders;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.netty4.server.myProxyServer.Tools;
import sample.netty4.server.myProxyServer.entity.AttributeKeys;
import sample.netty4.server.myProxyServer.entity.HttpResponse;
import sample.netty4.server.myProxyServer.enums.TransportProtocol;

public class HttpRequestHeaderHandler extends BaseInBoundHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHeaderHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Preconditions.checkArgument(msg instanceof FullHttpRequest);
        FullHttpRequest request = (FullHttpRequest) msg;

        try {
            String data = "";

            // host port
            data = request.headers().get(HttpHeaders.HOST);
            String hostAdd = "";
            int hostPort = 80;
            TransportProtocol tp = ctx.channel().attr(AttributeKeys.TRANSPORT_PROTOCOL).get();
            if (TransportProtocol.HTTPS == tp) {
                hostPort = 443;
            }
            if (StringUtils.isNotBlank(data)) {
                if (0 < data.indexOf(":")) {
                    String[] split = data.split(":");
                    hostAdd = split[0];
                    hostPort = Integer.parseInt(split[1]);
                } else {
                    hostAdd = data;
                }
                ctx.channel().attr(AttributeKeys.HOST_ADD).set(hostAdd);
                ctx.channel().attr(AttributeKeys.HOST_PORT).set(hostPort);
            } else {
                data = request.uri();
                if (0 < data.indexOf(":")) {
                    String[] split = data.split(":");
                    hostAdd = split[0];
                    hostPort = Integer.parseInt(split[1]);
                } else {
                    hostAdd = data;
                }
                ctx.channel().attr(AttributeKeys.HOST_ADD).set(hostAdd);
                ctx.channel().attr(AttributeKeys.HOST_PORT).set(hostPort);
            }

            // username password
            data = request.headers().get(HttpHeaders.PROXY_AUTHORIZATION);
            if (StringUtils.isNotBlank(data)) {
                data = new String(BaseEncoding.base64().decode(data.replace("Basic ", "")));
                String[] info = data.split(":");
                ctx.channel().attr(AttributeKeys.USERNAME).set(info[0]);
                ctx.channel().attr(AttributeKeys.PASSWORD).set(info[1]);
            }

            Tools.removeHeaders(request.headers());
            ctx.fireChannelRead(msg);
        } catch (Exception e) {
            logger.error("HttpRequestHeaderHandler ERR ", e);
            ctx.writeAndFlush(HttpResponse.BAD_GATEWAY).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
