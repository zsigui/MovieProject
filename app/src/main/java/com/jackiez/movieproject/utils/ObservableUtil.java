package com.jackiez.movieproject.utils;

import rx.Subscription;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/7
 */

public class ObservableUtil {

    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
