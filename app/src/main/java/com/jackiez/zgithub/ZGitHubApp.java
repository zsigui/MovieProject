package com.jackiez.zgithub;

import android.os.Looper;

import com.jackiez.base.SGApp;
import com.jackiez.zgithub.helper.CrashHandler;
import com.jackiez.zgithub.util.LaggyPrinter;

/**
 * Created by zsigui on 16-10-24.
 */

public class ZGitHubApp extends SGApp{


    private static ZGitHubApp sInstance;

    public static ZGitHubApp get() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    protected void doInitInMainProcess() {
        super.doInitInMainProcess();
        CrashHandler.init(getApplicationContext());
        // 原理一样，只是 BlockCanary 功能比较全先，对自身处理作用不明显
//        BlockCanary blockCanary = BlockCanary.install(this, new AppCanaryContext());
//        blockCanary.start();
        Looper.getMainLooper().setMessageLogging(new LaggyPrinter());
//        TinyDancer.create()
//                .redFlagPercentage(.1f)
//                .addFrameDataCallback(new FrameDataCallback() {
//                    @Override
//                    public void doFrame(long pre, long cur, int drop) {
//                        if (drop > 10) {
//                            AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "drop: " + drop);
//                        }
//                    }
//                })
//                .show(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
