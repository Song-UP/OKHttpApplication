package com.example.rxjava_retrofit.base;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Hawk.init(this).build();
    }
}
