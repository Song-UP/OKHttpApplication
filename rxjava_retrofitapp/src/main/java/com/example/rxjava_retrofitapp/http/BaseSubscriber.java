package com.example.rxjava_retrofitapp.http;

import android.content.Context;
import rx.Subscriber;

/**
 * Created by helin on 2016/10/10 15:49.
 *
 * 可以用来展示 等待控件，例如progressbar
 */

public  abstract class BaseSubscriber<T> extends Subscriber<T> implements ProgressCancelListener{
    public BaseSubscriber() {
    }
    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }
    /**
     * 显示Dialog
     */
    public void showProgressDialog(){
    }
    @Override
    public void onNext(T t) {
        _onNext(t);
    }
    /**
     * 隐藏Dialog
     */
    private void dismissProgressDialog(){
    }

    /**
     * 出现错误，预处理，把控件先隐藏
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (false) { //这里自行替换判断网络的代码
            _onError("网络不可用");
        } else if (e instanceof ApiException) {
            _onError(e.getMessage());
        } else {
            _onError("请求失败，请稍后再试...");
        }
        dismissProgressDialog();
    }
    /**
     * 有这个主要是Dialog 可能被手动取消，这个时候就需要，自动调用 unsubscribe() 取消订阅
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
    protected abstract void _onNext(T t);
    protected abstract void _onError(String message);
}
