package com.jackiez.movieproject.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/9
 */

public class UIUtil {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setTransfluentStatusBar(Activity activity) {
        if (activity == null) return;
        Window w = activity.getWindow();
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public static int getStatusBarSize(Context context) {
        return context.getResources().getDimensionPixelSize(
                context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }
}
