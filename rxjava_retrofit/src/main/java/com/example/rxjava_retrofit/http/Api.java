package com.example.rxjava_retrofit.http;

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

public class Api {
    private static ApiService service;
    /**
     * 请求超时
     */
    private static final int DEFAULT_TIMEOUT = 10000;
    public static ApiService getDefault(){
        if (service == null){
            OkHttpClient.Builder httClientBuilder = new OkHttpClient.Builder();
            httClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            /**
             * 拦截器，用于添加一些参数
             */
            httClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    HttpUrl.Builder authorizedUrlBuilder = request.url()
                            .newBuilder()
                            //添加手机唯一标识符，如token等
//                            .addQueryParameter("key1","value1")
//                            .addQueryParameter("key2", "valuel2")
                            ;
                    Request newRequest = request.newBuilder()
                            //对所有的请求添加请求头
                            // (所有的请求都是使用这个,此时可设置上传的数据类型-----这样其实也是把请求写的太死了，不利于后期扩展)
//                            .header("mobileFlag", "adfsaeef")
//                            .addHeader("head2", "head2")
                            .url(authorizedUrlBuilder.build())
                            .build();
                    return chain.proceed(newRequest);
                }
            });

            service = new Retrofit.Builder()
                    .client(httClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(Url.BASE_URL)
                    .build()
                    .create(ApiService.class);
        }

        return service;

    }


}

