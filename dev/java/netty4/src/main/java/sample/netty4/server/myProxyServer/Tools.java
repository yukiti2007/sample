package sample.netty4.server.myProxyServer;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.ssl.SslContext;
import sample.netty4.server.myProxyServer.entity.Constants;

import java.util.List;
import java.util.Map;

public final class Tools {

    public static HttpHeaders removeHeaders(HttpHeaders headers) {
        for (String header : Constants.DROP_HEADERS) {
            headers.remove(header);
        }
//        headers.add(com.google.common.net.HttpHeaders.CONNECTION, "close");

        return headers;
    }

    public static String readLine(ByteBuf in) {
        StringBuilder sb = new StringBuilder();

        char c = ' ';
        while ('\n' != c) {
            if (0 < in.readableBytes()) {
                c = (char) in.readByte();
                sb.append(c);
            } else {
                break;
            }
        }
        return sb.toString();
    }


    public static ChannelPipeline initHandler(Channel ch) {
        return initHandler(ch, null);
    }


    public static ChannelPipeline initHandler(Channel ch, SslContext sslCtx) {
        ChannelPipeline p = ch.pipeline();
        List<String> namesToRemove = Lists.newArrayList();
        for (Map.Entry<String, ChannelHandler> handlerEntry : p) {
            namesToRemove.add(handlerEntry.getKey());
        }
        for (String toRemove : namesToRemove) {
            p.remove(toRemove);
        }

        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
//        return pipeline
//                .addLast(new IdleStateHandler(0, 0, 60))
//                .addLast(IdleCloseHandler.INSTANCE);

        return p;
    }

    public static void removeHandler(ChannelPipeline p, Class<? extends ChannelHandler> clazz) {
        if (p.get(clazz) != null) {
            p.remove(clazz);
        }
    }
}
