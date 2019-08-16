package sample.netty4.server.myProxyServer.entity;

import com.google.common.collect.ImmutableSet;

public class Constants {

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
