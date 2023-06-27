package sample.okhttp3;

import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class httpProxyRequest {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = null;

    public static void main(String[] args) throws IOException {

        httpProxyRequest httpProxyRequest = new httpProxyRequest();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress("127.0.0.1", 8080));
        clientBuilder
                .proxy(proxy)
                .proxyAuthenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        if (response.request().header("Proxy-Authorization") != null) {
                            // Give up, we've already failed to authenticate.
                            return null;
                        }

                        String credential = Credentials.basic("proxyUserName", "proxyPassword11");
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                });
        httpProxyRequest.client = clientBuilder.build();

        System.out.println(httpProxyRequest.get("https://baidu.com"));
        System.out.println(httpProxyRequest.post("https://baidu.com", "{'aaa':'a1a1a1'}"));
    }

    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request httpRequest = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(httpRequest).execute()) {
            return response.body().string();
        }
    }
}
