package com.example.okhttpplus;

import android.app.Application;
import android.webkit.URLUtil;

import com.example.okhttpplus.model.User;
import com.example.okhttpplus.parser.JokeParser;
import com.example.okhttpplus.util.MyHostnameVerifier;
import com.example.okhttpplus.util.MyTrustManager;
import com.example.okhttpplus.util.TestUrls;
import com.socks.okhttp.plus.OkHttpProxy;
import com.socks.okhttp.plus.callback.OkCallback;
import com.socks.okhttp.plus.parser.OkJsonParser;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpPlusApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient.Builder builder = OkHttpProxy.getInstance().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(14, TimeUnit.SECONDS);
        //忽略https认证
        builder.hostnameVerifier(new MyHostnameVerifier());
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory());
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpProxy.setInstance(builder.build());
    }






}
