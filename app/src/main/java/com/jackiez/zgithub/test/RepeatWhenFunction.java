package com.jackiez.zgithub.test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by zsigui on 17-3-24.
 */

public class RepeatWhenFunction implements Function<Observable<Object>, ObservableSource<?>> {

    private static int INTERVAL_TIME = 2;
    private static int MAX_REPEAT_TIME = 3;


    @Override
    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
        return objectObservable.compose(new ObservableTransformer<Object, Long>() {
            @Override
            public ObservableSource<Long> apply(Observable<Object> upstream) {
                return upstream.zipWith(Observable.range(1, MAX_REPEAT_TIME), new BiFunction<Object, Integer, Integer>() {
                    @Override
                    public Integer apply(Object o, Integer integer) throws Exception {
                        return integer;
                    }
                }).flatMap(new Function<Integer, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Integer retryTime) throws Exception {
                        return Observable.timer((long) (INTERVAL_TIME * Math.pow(2, retryTime)), TimeUnit.SECONDS);
                    }
                });
            }
        });
    }
}
