package com.jackiez.base.util;

import android.os.Handler;
import android.os.Looper;

/**
 * 设置一个全局的Handler实例供共享，从而避免多处局部实例化出错
 *
 * Created by zsigui on 17-3-20.
 */

public class HandlerGlobal {

    /**
     * 新建一个全局的Handler处理器
     */
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static Handler get() {
        return sHandler;
    }

//    public static void post(Runnable r) {
//        sHandler.post(r);
//    }
//
//    public static void postDelay(Runnable r, long ms) {
//        sHandler.postDelayed(r, ms);
//    }
//
//    public static void post(Message msg) {
//        sHandler.sendMessage(msg);
//    }
//
//    public static void removeAll() {
//        sHandler.removeCallbacksAndMessages(null);
//    }
//
//    public static void removeCallabck(Runnable r) {
//        sHandler.removeCallbacks(r);
//    }

}
