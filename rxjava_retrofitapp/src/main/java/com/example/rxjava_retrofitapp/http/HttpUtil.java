package com.example.rxjava_retrofitapp.http;

import com.example.rxjava_retrofitapp.base.ActivityLifeCycleEvent;
import com.example.rxjava_retrofitapp.been.HttpResult;
import com.example.rxjava_retrofitapp.been.UserEntity;

import java.util.Observable;

import okhttp3.HttpUrl;
import rx.functions.Action0;
import rx.subjects.PublishSubject;

public class HttpUtil<T> {
    /**
     * 构造方法私有化
     */
    private HttpUtil(){
    }
    /**
     * 在访问HttpMethod时创建单例
     */
    private static class SingletonHolder{
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    public static HttpUtil getInstance(){
        return SingletonHolder.INSTANCE;
    }

    /**
     * 添加线程管理并订阅
     */
    public void toSubscribe(rx.Observable<T> ob, final BaseSubscriber<T> subscriber, String cacheKey,
                            final ActivityLifeCycleEvent event, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject,
                            boolean isSave, boolean forceRefresh) {
        //数据预处理
        rx.Observable.Transformer<Object, Object> result = RxHelper.handleResult(event,lifecycleSubject);
        rx.Observable observable = ob.compose(result)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //显示Dialog和一些其他操作
                        subscriber.showProgressDialog();
                    }
                });
        RetrofitCache.load(cacheKey,observable,isSave,forceRefresh).subscribe(subscriber);

    }




}
