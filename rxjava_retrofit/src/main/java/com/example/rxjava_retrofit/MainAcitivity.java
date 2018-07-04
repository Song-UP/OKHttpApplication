package com.example.rxjava_retrofit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.rxjava_retrofit.base.ActivityLifeCycleEvent;
import com.example.rxjava_retrofit.been.HttpResult;
import com.example.rxjava_retrofit.been.UserEntity;
import com.example.rxjava_retrofit.http.Api;
import com.example.rxjava_retrofit.http.HttpUtil;

import java.util.Observable;

import rx.subjects.PublishSubject;

public class MainAcitivity extends AppCompatActivity {

    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /** Retroift + rxjava 使用**/
    public void doGet(){
        rx.Observable<HttpResult<UserEntity>> observable = Api.getDefault().login("15103893286", "1234");
        HttpUtil.getInstance().toSubscribe(observable, "cacheKey",
                ActivityLifeCycleEvent.DESTROY, lifecycleSubject, );


    }

}
