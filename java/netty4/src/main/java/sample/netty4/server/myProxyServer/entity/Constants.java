package sample.netty4.server.myProxyServer.entity;

import com.google.common.collect.ImmutableSet;

public class Constants {

    // 代理协议
    public static final String PROXY_PROTOCOL_HTTP = "HTTP";
    public static final String PROXY_PROTOCOL_SOCKS4 = "SOCKS4";
    public static final String PROXY_PROTOCOL_SOCKS5 = "SOCKS5";
    // 传输协议
    public static final String TRANSPORT_PROTOCOL_HTTP = "HTTP";
    public static final String TRANSPORT_PROTOCOL_HTTPS = "HTTPS";

    public static final int CONNECT_TIMOUT_MS = 20000;

    public static final ImmutableSet HTTP_METHOD = (new ImmutableSet.Builder())
            .add("HEAD")
            .add("GET")
            .add("POST")
            .add("PUT")
            .add("DELETE")
            .build();

    public static final ImmutableSet HTTPS_METHOD = (new ImmutableSet.Builder())
            .add("CONNECT")
            .build();
}
