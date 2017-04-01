package com.jackiez.base.listener;

import android.content.Intent;

/**
 * Created by zsigui on 17-3-20.
 */

public interface ActivityLifeCycle {

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onNewIntent(Intent intent);

    void onResumeFragment();
}
