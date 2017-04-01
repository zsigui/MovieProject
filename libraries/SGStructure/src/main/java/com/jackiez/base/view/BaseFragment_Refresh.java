package com.jackiez.base.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by zsigui on 17-3-14.
 */

public class BaseFragment_Refresh extends BaseFragment {


    protected boolean isAllowRefresh() {
        return true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        // 执行网络请求
    }
}
