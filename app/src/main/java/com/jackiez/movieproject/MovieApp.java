package com.jackiez.movieproject;

import android.app.Application;
import android.content.Context;

import com.jackiez.movieproject.utils.AppUtil;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

public class MovieApp extends Application {

    private static MovieApp sInstance;

    public static MovieApp getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sInstance = this;
    }


    @Override
    public void onCreate() {
        if (AppUtil.isInMainProcess(getApplicationContext())) {
            appInitForMain();
        }
        appInitEveryTime();
    }

    /**
     * 在此执行应用每次初始化时都要执行的操作（即在多进程中重复操作）
     */
    private void appInitEveryTime() {

    }

    /**
     * 在此执行只须在主进程初始的操作
     */
    private void appInitForMain() {
//        Fresco.initialize(this);
    }

}
