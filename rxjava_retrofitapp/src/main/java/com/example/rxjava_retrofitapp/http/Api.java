package com.example.rxjava_retrofitapp.http;

import com.example.rxjava_retrofitapp.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by helin on 2016/11/10 10:28.
 *
 * 请求借口
 *
 */

public class Api {
    private static ApiService SERVICE;
    /**
     * 请求超时时间
     */
    private static final int DEFAULT_TIMEOUT = 10000;

    public static ApiService getDefault() {
        if (SERVICE == null) {
            //手动创建一个OkHttpClient并设置超时时间
            final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

            /**
             *  拦截器
             */
            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();

//                    Request.Builder requestBuilder = request.newBuilder();
//                    RequestBody formBody = new FormBody.Builder()
//                            .add("1","2")
//                            .build();

                    HttpUrl.Builder authorizedUrlBuilder = request.url()
                            .newBuilder()
/***
 //添加统一参数 如手机唯一标识符,token等
 //                            .addQueryParameter("key1","value1")
 //                            .addQueryParameter("key2", "value2")

 **/
                            ;

                    Request newRequest = request.newBuilder()
                            //对所有请求添加请求头
                            /**
                             .header("mobileFlag", "adfsaeefe").addHeader("type", "4")
                             **/
                            .header("Content-Type", "application/json")
                            .method(request.method(), request.body())
                            .url(authorizedUrlBuilder.build())
                            .build();

//                    okhttp3.Response originalResponse = chain.proceed(request);
//                    return originalResponse.newBuilder().header("mobileFlag", "adfsaeefe").addHeader("type", "4").build();



                    return  chain.proceed(newRequest);
                }
            });


            if (BuildConfig.DEBUG){
                httpClientBuilder.addInterceptor(new LoggingInterceptor());
            }
            SERVICE = new Retrofit.Builder()
                    .client(httpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(Url.BASE_URL)
                    .build().create(ApiService.class);
        }
        return SERVICE;
    }

}
