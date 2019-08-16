package sample.netty4.server.myProxyServer;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import com.google.common.net.HttpHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import sample.netty4.server.myProxyServer.entity.AttributeKeys;

import java.util.List;

public class RequestHeaderDecoder extends ByteToMessageDecoder {
    public static final ImmutableSet<String> DROP_HEADERS = (new ImmutableSet.Builder())
            .add(HttpHeaders.PROXY_AUTHORIZATION)
            .build();

    /**
     * Decode the from one {@link ByteBuf} to an other. This method will be called till either the input
     * {@link ByteBuf} has nothing to read when return from this method or till nothing was read from the input
     * {@link ByteBuf}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in  the {@link ByteBuf} from which to read data
     * @param out the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String line = "";
        try {
            while (!("\r\n".equals(line) || "\n".equals(line))) {
                boolean dropFlg = false;
                line = Tools.readLine(in);
                if (line.startsWith(HttpHeaders.PROXY_AUTHORIZATION)) {
                    String data = line.replace("Proxy-Authorization: Basic ", "");
                    data = data.substring(0, data.length() - 2);
                    data = new String(BaseEncoding.base64().decode(data));
                    String[] info = data.split(":");
                    if (2 == info.length) {
                        ctx.channel().attr(AttributeKeys.USERNAME).set(info[0]);
                        ctx.channel().attr(AttributeKeys.PASSWORD).set(info[1]);
                    }
                }

                for (String dropKey : DROP_HEADERS) {
                    if (line.startsWith(dropKey)) {
                        dropFlg = true;
                        break;
                    }
                }
                if (!dropFlg) {
                    out.add(line);
                }
            }
            byte[] data = new byte[in.readableBytes()];
            in.readBytes(data);
            out.add(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
