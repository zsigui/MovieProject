package com.jackiez.base.assist.rx;


import com.jackiez.base.entity.json.JsonResponse;
import com.jackiez.base.util.AppDebugLog;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


/**
 * Created by zsigui on 16-11-24.
 */

public class BaseSubscriber<T extends JsonResponse> implements Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, e);
    }

    @Override
    public void onComplete() {
        AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "onCompleted 请求执行操作完成!");
    }

    @Override
    public void onSubscribe(Subscription s) {
    }

    @Override
    public void onNext(T o) {
        if (o != null && !o.isValid()) {
            // 处理错误1
            // 处理统一错误2
            AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, o.errMsg());
            // 表示执行失败，被统一处理了
        }
    }
}
