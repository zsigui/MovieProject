package com.jackiez.base;

import android.app.Application;

import com.jackiez.base.assist.u_imageloader.SGImageLoader;
import com.jackiez.base.util.ProcessUtil;

/**
 * Created by zsigui on 17-3-14.
 */

public class SGApp extends Application {


    @Override
    public void onCreate() {
        SGImageLoader.get().init();
        if (ProcessUtil.isInMainProcess(this)) {
            doInitInMainProcess();
        }
    }

    protected void doInitInMainProcess() {

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        SGImageLoader.get().onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        SGImageLoader.get().onLowMemory();
    }
}
