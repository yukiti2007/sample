package sample.okhttp4;

import okhttp3.*;

import java.io.IOException;

public class HttpRequest {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        System.out.println(httpRequest.get("https://baidu.com"));
        System.out.println(httpRequest.post("https://baidu.com", "{'aaa':'a1a1a1'}"));
    }


    private String get(String url) throws IOException {
        okhttp3.Request request = new okhttp3.Request.Builder()
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
