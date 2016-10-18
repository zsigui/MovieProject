package com.jackiez.movieproject.utils;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/9
 */

public class UIUtil {

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setTranslucentStatusBar(Activity activity) {
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

    public static void fitViewMargin(View v, int topMargin) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).topMargin = topMargin;
            v.setLayoutParams(lp);
        }
    }

    public static void animateScale(View v, Animator.AnimatorListener listener) {
        v.animate().scaleY(1)
                .scaleX(1)
                .alpha(1)
                .setDuration(800)
                .setListener(listener);
    }

    public static Bitmap drawableToBitmap(Drawable d) {
        Bitmap bitmap = null;

        if (d instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) d;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (d.getIntrinsicWidth() <= 0 || d.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of
            // 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealShow(View v, Animator.AnimatorListener listener) {
        int cx = (v.getLeft() + v.getRight()) / 2;
        int cy = 0;
        int finalRadius = Math.max(v.getWidth(), v.getHeight());

        AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "cx = " + cx + ", cy = " + cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);
        v.setAlpha(1);
        anim.setDuration(800);
        anim.setInterpolator(new AccelerateInterpolator());
        if (listener != null) {
            anim.addListener(listener);
        }
        anim.start();

    }
}
