package com.jackiez.base.util;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * 跟进程/执行命令相关的操作工具类集合
 *
 * Created by zsigui on 17-3-20.
 */
public class ProcessUtil {

    /**
     * 判断当前是否处于应用主进程中
     */
    public static boolean isInMainProcess(@NonNull Context context) {
        return isInNamedProcess(context, context.getPackageName());
    }

    /**
     * 判断处于指定名称的进程中
     */
    public static boolean isInNamedProcess(Context context, String processName) {
        if (context == null || processName == null) {
            AppDebugLog.d(AppDebugLog.TAG_UTIL, "Error: null context or process name!");
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && processName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
