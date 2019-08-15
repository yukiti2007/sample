package sample.netty4.server.myProxyServer;

import io.netty.buffer.ByteBuf;

public final class Tools {

    static String readLine(ByteBuf in) {
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
}
