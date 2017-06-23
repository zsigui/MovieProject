package com.jackiez.base.rxbus2;

/**
 * Created by zsigui on 17-5-9.
 */

public interface ThreadMode {
    /**
     * current thread
     */
    int CURRENT_THREAD = 0;

    /**
     * android main thread
     */
    int MAIN = 1;


    /**
     * new thread
     */
    int NEW_THREAD = 2;
}
