package com.jackiez.base.listener;

import android.view.View;

/**
 * Created by zsigui on 17-3-27.
 */

public interface OnRetryListener {

    void onRetryWhenError(View v);

    void onRetryWhenEmpty(View v);
}
