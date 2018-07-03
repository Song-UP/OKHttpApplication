package com.example.rxjava_retrofit.http;

import com.example.rxjava_retrofit.been.HttpResult;
import com.example.rxjava_retrofit.been.UserEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    @GET("/student/mobileRegister")
    Observable<HttpResult<UserEntity>> login(@Query("phone") String phone, @Query("password") String psw);

}
