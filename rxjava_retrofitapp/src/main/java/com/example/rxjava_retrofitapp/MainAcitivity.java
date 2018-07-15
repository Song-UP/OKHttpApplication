package com.example.rxjava_retrofitapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.rxjava_retrofitapp.base.ActivityLifeCycleEvent;
import com.example.rxjava_retrofitapp.been.HttpResult;
import com.example.rxjava_retrofitapp.been.UserEntity;
import com.example.rxjava_retrofitapp.http.Api;
import com.example.rxjava_retrofitapp.http.BaseSubscriber;
import com.example.rxjava_retrofitapp.http.HttpUtil;
import com.example.rxjava_retrofitapp.http.ProgressSubscriber;

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
        rx.Observable<UserEntity> observable = Api.getDefault().login("15103893286", "1234");
        HttpUtil.getInstance().toSubscribe(observable, new BaseSubscriber<UserEntity>() {
            @Override
            protected void _onNext(UserEntity userEntity) {

            }

            @Override
            protected void _onError(String message) {

            }
        },
                "cacheKey", ActivityLifeCycleEvent.DESTROY, lifecycleSubject, false, false);



//        observable,
//                new ProgressSubscriber<UserEntity>(this) {
//                    @Override
//                    protected void _onNext(UserEntity o) {
//
//                    }
//
//                    @Override
//                    protected void _onError(String message) {
//
//                    }
//                },
//                "cacheKey", ActivityLifeCycleEvent.DESTROY, lifecycleSubject, false,false

    }

}
