package com.jackiez.zgithub.model.net.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * 默认RX请求之后进行重复轮询的处理函数
 * <p>
 * Created by zsigui on 17-3-24.
 */

public class RxRepeatWhenFunc implements Function<Observable<Object>, ObservableSource<?>>, GetTimeHandler {

    /**
     * 最大重试次数
     */
    private static final int MAX_REPEAT_TIME = 3;
    /**
     * 没执行一次间隔时间
     */
    public static final int INTERVAL_TIME = 400;


    /**
     * 最大循环次数，当 repeat <= 0，表示无限制
     */
    private int maxRepeatTime;

    private GetTimeHandler getTimeHandler;

    public RxRepeatWhenFunc() {
        this(MAX_REPEAT_TIME);
    }

    public RxRepeatWhenFunc(int maxRepeatTime) {
        this(maxRepeatTime, null);
    }

    public RxRepeatWhenFunc(int maxRepeatTime, GetTimeHandler getTimeHandler) {
        this.maxRepeatTime = maxRepeatTime;
        this.getTimeHandler = getTimeHandler;
        if (this.maxRepeatTime <= 0) {
            this.maxRepeatTime = Integer.MAX_VALUE;
        }
    }

    @Override
    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
        return objectObservable.compose(new ObservableTransformer<Object, Long>() {
            @Override
            public ObservableSource<Long> apply(Observable<Object> upstream) {
                return upstream.zipWith(Observable.range(1, maxRepeatTime), new BiFunction<Object, Integer,
                        Integer>() {

                    @Override
                    public Integer apply(Object o, Integer integer) throws Exception {
                        return integer;
                    }
                }).flatMap(new Function<Integer, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Integer repeatTime) throws Exception {
                        int diff = (getTimeHandler == null ?
                                calTime(repeatTime) : getTimeHandler.calTime(repeatTime));
                        return Observable.timer(diff, TimeUnit.MILLISECONDS);
                    }
                });

            }
        });
    }

    @Override
    public int calTime(int n) {
        return INTERVAL_TIME;
    }
}
