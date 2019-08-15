package sample.netty4.server.myProxyServer;

import com.google.common.io.BaseEncoding;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.netty4.server.myProxyServer.entity.HttpResponse;

@ChannelHandler.Sharable
public class HttpProxyAuthorizationHander extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpProxyAuthorizationHander.class);

    public static HttpProxyAuthorizationHander INSTANCE = new HttpProxyAuthorizationHander();

    private HttpProxyAuthorizationHander() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String line = "";
        String userName = "";
        String password = "";
        try {
            long start = System.currentTimeMillis();
            // 标记读位置
            in.markReaderIndex();
            while (!("\r\n".equals(line) || "\n".equals(line))) {
                line = Tools.readLine(in);
                if (line.startsWith("Proxy-Authorization:")) {
                    String data = line.replace("Proxy-Authorization: Basic ", "");
                    data = data.substring(0, data.length() - 2);
                    data = new String(BaseEncoding.base64().decode(data));
                    String[] info = data.split(":");
                    if (2 == info.length) {
                        userName = info[0];
                        password = info[1];
                    }
                }
            }
            System.out.println("Proxy-Authorization exTime:" + (System.currentTimeMillis() - start));
        } finally {
            if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
                ctx.channel().writeAndFlush(new HttpResponse(HttpResponseStatus.UNAUTHORIZED)).addListener(ChannelFutureListener.CLOSE);
                ReferenceCountUtil.release(msg);
            } else {
                in.resetReaderIndex();
                ctx.fireChannelRead(msg);
            }
        }
    }
}
