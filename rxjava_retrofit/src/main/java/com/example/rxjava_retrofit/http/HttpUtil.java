package com.example.rxjava_retrofit.http;

import com.example.rxjava_retrofit.base.ActivityLifeCycleEvent;
import com.example.rxjava_retrofit.been.HttpResult;

import java.util.Observable;

import okhttp3.HttpUrl;
import rx.functions.Action0;
import rx.subjects.PublishSubject;

public class HttpUtil {
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
    public void toSubscribe(rx.Observable ob, final ProgressSubscriber subscriber, String cacheKey, final ActivityLifeCycleEvent event, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject, boolean isSave, boolean forceRefresh) {
        //数据预处理
        rx.Observable.Transformer<HttpResult<Object>, Object>
                result = RxHelper.handleResult(event,lifecycleSubject);
        rx.Observable observable = ob.compose(result)
                .doOnSubscribe(new Action0() {   //可以进行预处理（默认在所在线程，不然就是离得最近的）
                    @Override
                    public void call() {
                        subscriber.showProgressDialog();
                    }
                });
        RetrofitCache.load(cacheKey,observable, isSave, forceRefresh ).subscribe(subscriber);

    }




}
