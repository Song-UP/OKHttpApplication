package com.example.myokhttpdemo;

import android.app.Application;
import android.app.DownloadManager;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApplication extends Application {
    private static MyApplication myApplication;
    private MyOkHttp myOkHttp;
    private DownloadMgr downloadMgr;

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;
        //持久化cookie
        ClearableCookieJar clearableCookieJar =
                new PersistentCookieJar(new SetCookieCache(),new SharedPrefsCookiePersistor(getApplicationContext()));
        //log拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //自定义Okttp
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.SECONDS)
                .readTimeout(10000L, TimeUnit.SECONDS)
                .writeTimeout(10000L, TimeUnit.SECONDS)
                .cookieJar(clearableCookieJar)
                .addInterceptor(logging)
                .build();
        myOkHttp = new MyOkHttp(okHttpClient);

        //
        downloadMgr = (DownloadMgr) new DownloadMgr.Builder()
                        .myOkHttp(myOkHttp)
                        .maxDownloadIngNum(5) //设置最大同时下载数量（不设置默认5）
                        .saveProgressBytes(50*1024) //设置每50kb触发一次saveProgress保存进度 （不能在onProgress每次都保存 过于频繁） 不设置默认50kb
                        .build();
        downloadMgr.resumeTasks();  //恢复本地所有未完成的任务

    }

    public static synchronized MyApplication getInstance() {
        return myApplication;
    }

    public MyOkHttp getMyOkHttp() {
        return myOkHttp;
    }

    public DownloadMgr getDownloadMgr() {
        return downloadMgr;
    }
}
