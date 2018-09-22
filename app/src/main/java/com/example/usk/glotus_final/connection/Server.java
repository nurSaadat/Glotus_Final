package com.example.usk.glotus_final.connection;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Server {
    public static String credential;
    public static int status;
    public static String url;
    public static String way;
    public static String body;
    public static String ans;

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody data;

    public String res;
    public Server(){
    }

    public Server(String url){
        this.url=url;
    }
    public Server(String url, String credential){
        this.url=url;
        this.credential=credential;
    }
    public Server(String url, String credential, String body){
        this.url=url;
        this.credential=credential;
        this.body=body;
    }




    public String get() {
        System.out.println("--------------");
        if (credential==null){
            System.out.println("--------------------");
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        return connect(request);
        }
        else {
            System.out.println("--------------------");
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("authorization",credential)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            return connect(request);

        }
    }

    public String post() {
        data = RequestBody.create(mediaType, body);
        if (credential==null){
            Request request = new Request.Builder()
                    .url(url)
                    .post(data)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            return connect(request);
        }
        else {
            Request request = new Request.Builder()
                    .url(url)
                    .post(data)
                    .addHeader("authorization", credential)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();
            return connect(request);
        }

    }

    public String connect(Request request) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                status=-1;
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    res=response.body().string();
                    status=1;
                    countDownLatch.countDown();
                } else {
                    status=0;
                    res=response.body().string();
                    countDownLatch.countDown();
                }
            }});
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return res;
    }
    public static String getBody() {
        return body;
    }
    public static String getCredential() {
        return credential;
    }
    public static int getStatus() {
        return status;
    }
    public static String getUrl() {
        return url;
    }
    public static String getWay() {
        return way;
    }
    public static void setBody(String body) {
        Server.body = body;
    }
    public static void setCredential(String credential) {
        Server.credential = credential;
    }
    public static void setStatus(int status) {
        Server.status = status;
    }
    public static void setUrl(String url) {
        Server.url = url;
    }
    public static void setWay(String way) {
        Server.way = way;
    }
    public String getRes() {
        return res;
    }
    public static String getAns() {
        return ans;
    }


}
