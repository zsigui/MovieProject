package com.jackiez.zgithub.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.jackiez.base.util.AppDebugLog;

/**
 * Created by zsigui on 17-3-22.
 */

public class DialogHelper {

    static ProgressDialog mProgressDialog;

    public static void setCancelListener(DialogInterface.OnCancelListener listener) {
        if (mProgressDialog != null) {
            mProgressDialog.setOnCancelListener(listener);
        }
    }

    /**
     * 此处 Dialog 传入 Context 需要为 Activity Context 而不能是 Application Context，否则会出现 Token 异常。 <br />
     * 如果需要使用 Application Context，需要将该处弹窗修改为通过 WindowManager 添加的方式。
     */
    public static void showProgressDialog(Context c) {
        if (c == null) {
            AppDebugLog.d(AppDebugLog.TAG_UTIL, "context == null");
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(c, android.R.style.Widget_ProgressBar_Large);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(true);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }
}
