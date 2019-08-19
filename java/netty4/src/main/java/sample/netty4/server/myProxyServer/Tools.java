package sample.netty4.server.myProxyServer;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import sample.netty4.server.myProxyServer.handler.IdleCloseHandler;

import java.util.List;
import java.util.Map;

public final class Tools {

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


    public static ChannelPipeline initHandler(ChannelPipeline pipeline) {
        List<String> namesToRemove = Lists.newArrayList();
        for (Map.Entry<String, ChannelHandler> handlerEntry : pipeline) {
            namesToRemove.add(handlerEntry.getKey());
        }
        for (String toRemove : namesToRemove) {
            pipeline.remove(toRemove);
        }
//        return pipeline
//                .addLast(new IdleStateHandler(0, 0, 60))
//                .addLast(IdleCloseHandler.INSTANCE);

        return pipeline;
    }

    public static void removeHandler(ChannelPipeline p, Class<? extends ChannelHandler> clazz) {
        if (p.get(clazz) != null) {
            p.remove(clazz);
        }
    }
}
