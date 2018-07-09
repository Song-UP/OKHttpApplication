package com.example.rxjava_retrofitapp.http;

import com.example.rxjava_retrofitapp.been.HttpResult;
import com.example.rxjava_retrofitapp.been.UserEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    @GET("/student/mobileRegister")
    Observable<HttpResult<UserEntity>> login(@Query("phone") String phone, @Query("password") String psw);

}
