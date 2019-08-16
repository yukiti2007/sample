package sample.netty4.server.myProxyServer.entity;

import io.netty.util.AttributeKey;

public class AttributeKeys {
    public static final AttributeKey<String> USERNAME = AttributeKey.valueOf("username");
    public static final AttributeKey<String> PASSWORD = AttributeKey.valueOf("password");
    public static final AttributeKey<String> HTTP_METHOD = AttributeKey.valueOf("http_method");
}
