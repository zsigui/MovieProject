package com.jackiez.base.assist.rx;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by zsigui on 16-11-24.
 */

public class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {

    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.error(ExceptionHandler.handleException(throwable));
    }
}
