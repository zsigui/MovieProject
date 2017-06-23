package com.jackiez.zgithub.test;

/**
 * Created by zsigui on 17-3-23.
 */

public class BusSingleton {

    private static com.jackiez.base.rxbus2.RxBus bs;

    public static com.jackiez.base.rxbus2.RxBus get() {
        return com.jackiez.base.rxbus2.RxBus.get();
    }
}
