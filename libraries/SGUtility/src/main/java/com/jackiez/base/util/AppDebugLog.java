package com.jackiez.base.util;

import android.content.Context;

import com.jackiez.base.log.SGLog;

/**
 * Created by zsigui on 16-9-29.
 */

public class AppDebugLog {
    /**
     * debug模式，发布打包需要置为false，可以通过混淆让调试的log文本从代码文件中消除，避免被反编译时漏泄相关信息。
     */
    public static boolean IS_DEBUG = BuildConfig.LOG_DEBUG;
    private static boolean IS_FILE_DEBUG = BuildConfig.FILE_DEBUG;


    private static final String TAG_PRE = "sg_";

    public static final String TAG_APP = TAG_PRE + "app";

    public static final String TAG_FRAG = TAG_PRE + "fragment";

    public static final String TAG_UTIL = TAG_PRE + "util";

    public static final String TAG_DEBUG_INFO = TAG_PRE + "info";

    public static final String TAG_NET = TAG_PRE + "net";

    public static final String TAG_THROWABLE = TAG_PRE + "throwable";
    /**
     * 所有注解该TAG的LOG需要进行删除
     */
    public static final String TAG_WARN = TAG_PRE + "warning";

    public static final int STACKTRACE_INDEX = 6;

    public static void w(String tag, Object... object) {
        w(STACKTRACE_INDEX + 1, tag, object);
    }

    public static void w(int stacktraceIndex, String tag, Object... object) {
        if (AppDebugLog.IS_DEBUG) {
            SGLog.w(stacktraceIndex, tag, object);
        }
    }

    public static void v() {
        if (AppDebugLog.IS_DEBUG) {
            SGLog.v(STACKTRACE_INDEX, AppDebugLog.TAG_DEBUG_INFO);
        }
    }

    public static void v(String tag, Object... object) {
        if (AppDebugLog.IS_DEBUG) {
            SGLog.v(STACKTRACE_INDEX, tag, object);
        }
    }

    public static void d(Object object) {
        if (AppDebugLog.IS_DEBUG) {
            SGLog.d(STACKTRACE_INDEX, AppDebugLog.TAG_DEBUG_INFO, object);
        }
    }

    public static void d(String tag, Object... object) {
        if (AppDebugLog.IS_DEBUG) {
            SGLog.d(STACKTRACE_INDEX, tag, object);
        }
    }

    public static void e(Object object) {
        if (AppDebugLog.IS_DEBUG) {
            SGLog.e(STACKTRACE_INDEX, AppDebugLog.TAG_DEBUG_INFO, object);
        }
    }

    public static void e(String tag, Object... object) {
        if (AppDebugLog.IS_DEBUG) {
            SGLog.e(STACKTRACE_INDEX, tag, object);
        }
    }

    public static void file(Context context, Object object) {
        if (AppDebugLog.IS_DEBUG && AppDebugLog.IS_FILE_DEBUG) {
            SGLog.file(context, STACKTRACE_INDEX, AppDebugLog.TAG_DEBUG_INFO, null, null, object);
        }
    }

    public static void file(Context context, String tag, String filename, Object... object) {
        if (AppDebugLog.IS_DEBUG && AppDebugLog.IS_FILE_DEBUG) {
            SGLog.file(context, STACKTRACE_INDEX, tag, null, filename, object);
        }
    }
}