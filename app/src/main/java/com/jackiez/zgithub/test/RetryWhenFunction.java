package com.jackiez.zgithub.test;

import com.jackiez.base.util.AppDebugLog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by zsigui on 17-3-24.
 */
public class RetryWhenFunction implements Function<Observable<Throwable>, ObservableSource<?>> {


    private final int MAX_RETRY_TIME = 3;
    private int mRetryTime = 0;
    private final int INTERVAL_TIME = 100;

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                AppDebugLog.d(AppDebugLog.TAG_UTIL, "RetryWhenFunction当前所处线程: " + Thread.currentThread().getName());
                if (throwable instanceof HttpException) {
                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "RetryWhenFunction返回.error(HttpException)");
                    HttpException ex = (HttpException) throwable;
                    int code = ex.code();
                    if ((code != 408 && code != 423 && code != 424 && code != 449)
                            && code >= 400 && code  < 500) {
                        // 408 客户端请求超时，所以有重复发起请求的必要
                        // 423 资源被锁定，则过会重试是否继续锁定中
                        // 424 此前错误的请求导致当前失败，过会继续重试
                        // 449 微软扩展，表示请求需要在适当操作之后重试
                        // 客户端请求地址等错误，非服务器/网络原因，不重试直接返回
                        return Observable.error(throwable);
                    }
                }
                if (mRetryTime < MAX_RETRY_TIME) {
                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "RetryWhenFunction返回.timer()");
                    mRetryTime++;
                    return Observable.timer((long) (INTERVAL_TIME * Math.pow(2, mRetryTime)), TimeUnit.MILLISECONDS);
                }

                AppDebugLog.d(AppDebugLog.TAG_UTIL, "RetryWhenFunction返回.error()");
                return Observable.error(throwable);
            }
        });
    }
}
