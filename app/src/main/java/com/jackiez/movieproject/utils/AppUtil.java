package com.jackiez.movieproject.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by zsigui on 16-10-10.
 */

public class AppUtil {

    public static boolean isInMainProcess(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        int myPid = android.os.Process.myPid();
        String mainProcessName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
