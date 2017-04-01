package com.jackiez.zgithub.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import com.jackiez.base.util.AppDebugLog;

/**
 * Created by zsigui on 17-3-30.
 */

public class LaggyMonitor {


    private static final int DELAY_TIME = 1000;
    private boolean isM = false;

    private Handler mIoHandler;
    private HandlerThread mLogThread = new HandlerThread("log", Process.THREAD_PRIORITY_BACKGROUND);
    private Runnable mPrintStackRunnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stack = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stack) {
                sb.append(s.toString()).append("\n");
            }
            AppDebugLog.d(AppDebugLog.TAG_THROWABLE, sb.toString());
        }
    };

    private LaggyMonitor() {}

    private static LaggyMonitor sInstance;

    public static LaggyMonitor get() {
        if (sInstance == null) {
            synchronized (LaggyMonitor.class) {
                if (sInstance == null) {
                    sInstance = new LaggyMonitor();
                }
            }
        }
        return sInstance;
    }

    public boolean isMonitor() {
        return isM;
    }

    public void startMonitor() {
        if (mIoHandler == null) {
            mLogThread.start();
            mIoHandler = new Handler(mLogThread.getLooper());
        }
        stopMonitor();
        mIoHandler.postDelayed(mPrintStackRunnable, DELAY_TIME);
        isM = true;
    }

    public void stopMonitor() {
        if (mIoHandler != null && isM) {
            mIoHandler.removeCallbacks(mPrintStackRunnable);
        }
        isM = false;
    }

    /**
     * 直接在线程中打印当前堆栈消息
     */
    public void logStack() {
        mIoHandler.post(mPrintStackRunnable);
    }
}
