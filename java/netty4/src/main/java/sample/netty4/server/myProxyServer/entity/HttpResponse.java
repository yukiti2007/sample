package sample.netty4.server.myProxyServer.entity;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HttpResponse extends DefaultFullHttpResponse {

    private static final ByteBuf RESPONSE_BODY = Unpooled.copiedBuffer(Unpooled.EMPTY_BUFFER);

    // 4XX
    public static final HttpResponse PROXY_AUTHENTICATION_REQUIRED = new HttpResponse(HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED);

    // 5XX
    public static final HttpResponse BAD_GATEWAY = new HttpResponse(HttpResponseStatus.BAD_GATEWAY);
    public static final HttpResponse GATEWAY_TIMEOUT = new HttpResponse(HttpResponseStatus.GATEWAY_TIMEOUT);

    public HttpResponse(HttpResponseStatus responseStatus) {
        super(HttpVersion.HTTP_1_1, responseStatus, RESPONSE_BODY);
        headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=utf-8");
        headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        headers().set(HttpHeaderNames.CONNECTION, "close");
    }
}
