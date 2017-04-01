package com.jackiez.zgithub.model.net.rx;

import com.jackiez.base.assist.rx.ApiException;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * 默认进行RX请求之后用于错误重试的处理函数
 *
 * Created by zsigui on 17-3-24.
 */

public class RxRetryWhenFunc implements Function<Observable<Throwable>, ObservableSource<?>>, GetTimeHandler {

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRY_TIME = 3;
    /**
     * 执行二进制指数退避算法的间隔值，以 MS 为单位
     */
    public static final int INTERVAL_TIME = 400;
    /**
     * 当前已经重试次数
     */
    private int retryTime = 0;
    /**
     * 最大重试次数，当 retry <= 0，表示无限制
     */
    private int maxRetryTime;

    private GetTimeHandler getTimeHandler;

    public RxRetryWhenFunc() {
        this(MAX_RETRY_TIME);
    }

    public RxRetryWhenFunc(int maxRetryTime) {
        this(maxRetryTime, null);
    }

    public RxRetryWhenFunc(int maxRetryTime, GetTimeHandler getTimeHandler) {
        this.maxRetryTime = maxRetryTime;
        this.getTimeHandler = getTimeHandler;
        if (this.maxRetryTime <= 0) {
            this.maxRetryTime = Integer.MAX_VALUE;
        }
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {

                // 对于部分绝对失败情况不执行重试
                if (throwable instanceof ApiException) {
                    // 已正常请求服务器返回的信息，不再做处理，实际情况并不多见
                    return Observable.error(throwable);
                }
                if (throwable instanceof HttpException) {
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

                if (retryTime < maxRetryTime) {
                    retryTime++;
                    // 二进制指数退避算法
                    return Observable.timer(calTime(retryTime), TimeUnit.MILLISECONDS);
                }

                // 重试次数超限，直接返回异常
                return Observable.error(throwable);
            }
        });
    }

    // 默认执行二进制指数退避算法
    @Override
    public int calTime(int n) {
        return (int) (INTERVAL_TIME * Math.pow(2, retryTime));
    }
}
