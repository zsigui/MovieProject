package com.jackiez.base.assist.rx;

import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by zsigui on 16-11-24.
 */

public final class ExceptionHandler {

    private static final class HttpCode {
        static final int UNKNOWN = 1;
        static final int UNAUTHORIZED = 401;
        static final int FORBIDDEN = 403;
        static final int NOT_FOUND = 404;
        static final int REQUEST_TIMEOUT = 408;
        static final int INTERNAL_SERVER_ERROR = 500;
        static final int BAD_GATEWAY = 502;
        static final int SERVICE_UNAVAILABLE = 503;
        static final int GATEWAY_TIMEOUT = 504;
    }

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, httpException.code(), httpException.message());
        } else {
            ex = new ApiException(e, HttpCode.UNKNOWN, "");
        }
        return ex;
    }
}
