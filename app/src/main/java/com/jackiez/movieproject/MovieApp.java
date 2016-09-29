package com.jackiez.movieproject;

import android.app.Application;
import android.content.Context;

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
    public void onCreate() {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sInstance = this;
    }
}
