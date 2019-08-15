package sample.netty4.server.myProxyServer.entity;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HttpResponse extends DefaultFullHttpResponse {

    private static final ByteBuf RESPONSE_BODY = Unpooled.unreleasableBuffer(Unpooled.EMPTY_BUFFER);

    public HttpResponse(HttpResponseStatus responseStatus) {
        super(HttpVersion.HTTP_1_1, responseStatus, RESPONSE_BODY);
        headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=utf-8");
        headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        headers().set(HttpHeaderNames.CONNECTION, "close");
    }
}
