package com.jackiez.zgithub.test;

import com.google.common.eventbus.EventBus;

/**
 * Created by zsigui on 17-3-23.
 */

public class BusSingleton {

    private static EventBus bs;

    public static EventBus get() {
        if (bs == null)
            bs = new EventBus();
        return bs;
    }
}
