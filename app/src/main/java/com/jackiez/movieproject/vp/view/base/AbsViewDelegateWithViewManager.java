package com.jackiez.movieproject.vp.view.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.jackiez.movieproject.R;
import com.jackiez.movieproject.utils.AppDebugLog;
import com.jackiez.movieproject.views.widget.MultiStateViewManager;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/4
 */

public abstract class AbsViewDelegateWithViewManager<Binding extends ViewDataBinding> extends AbsViewDelegate<Binding> {

    protected MultiStateViewManager mViewManager;

    public AbsViewDelegateWithViewManager(Context context) {
        super(context);
        mViewManager = MultiStateViewManager.generate(context);
        mViewManager.setContentView(mBinding.getRoot());
        mViewManager.setEmptyView(R.layout.view_empty);
        mViewManager.setErrorRetryView(R.layout.view_error);
        mViewManager.setLoadView(R.layout.view_loading);
    }

    @Override
    public View getRootView() {
        return mViewManager.getContainer();
    }

    public void showLoading() {
        AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "show loading");
        mViewManager.showLoading();
    }

    public void showEmpty() {
        AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "show empty");
        mViewManager.showEmpty();
    }

    public void showError() {
        AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "show error");
        mViewManager.showErrorRetry();
    }

    public void showContent() {
        AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "show content");
        mViewManager.showContent();
    }
}
