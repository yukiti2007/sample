package sample.netty4.server.myProxyServer.handler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.net.HttpHeaders;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.netty4.server.myProxyServer.entity.AttributeKeys;
import sample.netty4.server.myProxyServer.entity.HttpResponse;
import sample.netty4.server.myProxyServer.enums.TransportProtocol;

public class HttpRequestHeaderHandler extends BaseInBoundHandler {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestHeaderHandler.class);

    public static final ImmutableSet<String> DROP_HEADERS =
            ImmutableSet.of(
                    HttpHeaders.PROXY_AUTHORIZATION,
                    HttpHeaders.X_FORWARDED_FOR);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Preconditions.checkArgument(msg instanceof FullHttpRequest);
        FullHttpRequest request = (FullHttpRequest) msg;
        try {
            String hostAdd = "";
            int hostPort = 80;
            TransportProtocol tp = ctx.channel().attr(AttributeKeys.TRANSPORT_PROTOCOL).get();
            if (TransportProtocol.HTTPS == tp) {
                hostPort = 443;
            }
            String host = request.headers().get(HttpHeaders.HOST);
            if (0 < host.indexOf(":")) {
                String[] split = host.split(":");
                hostAdd = split[0];
                hostPort = Integer.parseInt(split[1]);
            } else {
                hostAdd = host;
            }
            ctx.channel().attr(AttributeKeys.HOST_ADD).set(hostAdd);
            ctx.channel().attr(AttributeKeys.HOST_PORT).set(hostPort);


            io.netty.handler.codec.http.HttpHeaders headers = request.headers();
            for (String header : DROP_HEADERS) {
                headers.remove(header);
            }
            request.headers().add(HttpHeaders.CONNECTION, "close");
            ctx.fireChannelRead(msg);
        } catch (Exception e) {
            logger.error("HttpRequestHeaderHandler ERR ", e);
            ctx.writeAndFlush(HttpResponse.BAD_GATEWAY).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
