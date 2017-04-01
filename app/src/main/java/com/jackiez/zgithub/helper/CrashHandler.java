package com.jackiez.zgithub.helper;

import android.content.Context;
import android.support.compat.BuildConfig;

import com.jackiez.base.util.AppDebugLog;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by zsigui on 17-3-23.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{

//    @SuppressLint("StaticFieldLeak")
//    private static CrashHandler sInstance;
//
//    private static CrashHandler get() {
//        if (sInstance == null) {
//            sInstance = new CrashHandler();
//        }
//        return sInstance;
//    }

    private Context mContext;
    /**
     * 保留的系统处理
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 初始化设置 崩溃收集
     */
    public static void init(Context context) {
        CrashHandler c = new CrashHandler();
        c.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        c.mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(c);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        AppDebugLog.e(AppDebugLog.TAG_THROWABLE, e.toString());
        AppDebugLog.e(AppDebugLog.TAG_THROWABLE, collectCrashDeviceInfo());
        AppDebugLog.e(AppDebugLog.TAG_THROWABLE, getCrashInfo(e));
        // 收集存储数据，然后后续进行崩溃数据上传

        // 调用系统错误机制
        mDefaultHandler.uncaughtException(t, e);
    }

    /**
     * 得到程序崩溃的详细信息
     */
    public String getCrashInfo(Throwable ex) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        ex.setStackTrace(ex.getStackTrace());
        ex.printStackTrace(printWriter);
        return result.toString();
    }

    /**
     * 收集程序崩溃的设备信息
     */
    public String collectCrashDeviceInfo() {

        String versionName = BuildConfig.VERSION_NAME;
        String versionCode = String.valueOf(BuildConfig.VERSION_CODE);
        String model = android.os.Build.MODEL;
        String androidVersion = android.os.Build.VERSION.RELEASE;
        String manufacturer = android.os.Build.MANUFACTURER;

        return versionCode + "  " + versionName + "  " + model + "  " + androidVersion + "  " + manufacturer;
    }

}
