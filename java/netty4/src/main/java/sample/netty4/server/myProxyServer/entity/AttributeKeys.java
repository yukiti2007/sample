package sample.netty4.server.myProxyServer.entity;

import io.netty.util.AttributeKey;
import sample.netty4.server.myProxyServer.enums.ProxyProtocol;
import sample.netty4.server.myProxyServer.enums.TransportProtocol;

public class AttributeKeys {
    public static final AttributeKey<String> USERNAME = AttributeKey.valueOf("username");
    public static final AttributeKey<String> HOST = AttributeKey.valueOf("host");
    public static final AttributeKey<String> HOST_ADD = AttributeKey.valueOf("host_add");
    public static final AttributeKey<Integer> HOST_PORT = AttributeKey.valueOf("host_port");
    public static final AttributeKey<String> PROXY_ADD = AttributeKey.valueOf("proxy_add");
    public static final AttributeKey<Integer> PROXY_PORT = AttributeKey.valueOf("proxy_port");
    public static final AttributeKey<Integer> CONNECT_TIMOUT_MS = AttributeKey.valueOf("connect_timout_ms");
    public static final AttributeKey<ProxyProtocol> PROXY_PROTOCOL = AttributeKey.valueOf("proxy_protocol");
    public static final AttributeKey<TransportProtocol> TRANSPORT_PROTOCOL = AttributeKey.valueOf("transport_protocol");
}
