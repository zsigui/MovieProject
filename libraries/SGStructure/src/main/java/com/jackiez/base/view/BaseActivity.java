package com.jackiez.base.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.jackiez.base.assist.u_imageloader.SGImageLoader;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;

/**
 * Created by zsigui on 17-3-9.
 */

public abstract class BaseActivity extends SupportActivity {

    private FragmentLifecycleCallbacks mLifeCycleCallback = new FragmentLifecycleCallbacks();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        registerFragmentLifecycleCallbacks(mLifeCycleCallback);
    }

    @LayoutRes
    protected abstract int getContentViewId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterFragmentLifecycleCallbacks(mLifeCycleCallback);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        SGImageLoader.get().onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        SGImageLoader.get().onLowMemory();
    }

    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
    }
}
