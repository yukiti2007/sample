package sample.netty4.server.myProxyServer.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.net.HttpHeaders;

public class Constants {

    public static final int CONNECT_TIMOUT_MS = 20000;

    public static final ImmutableSet<String> DROP_HEADERS = ImmutableSet.of(
            HttpHeaders.PROXY_AUTHORIZATION,
            HttpHeaders.X_FORWARDED_FOR);

    public static final ImmutableSet HTTP_METHOD = ImmutableSet.of(
            "HEAD", "GET", "POST", "PUT", "DELETE");

    public static final ImmutableSet HTTPS_METHOD = ImmutableSet.of(
            "CONNECT");
}
