package com.jackiez.movieproject.vp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jackiez.movieproject.R;
import com.jackiez.movieproject.utils.AppDebugLog;
import com.jackiez.movieproject.utils.NetworkUtil;
import com.jackiez.movieproject.views.widget.MultiStateViewManager;
import com.jackiez.movieproject.vp.view.base.AbsViewDelegateWithViewManager;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/4
 */

public abstract class AbsPresenterWithViewManager<Data, ViewDelegate extends AbsViewDelegateWithViewManager>
        extends AbsPresenter<Data, ViewDelegate> implements MultiStateViewManager.OnRetryOrEmptyListener, View
        .OnClickListener {

    protected boolean mInRefreshing = false;

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        super.processLogic(savedInstanceState);
    }

    @Override
    protected void setListener() {
        super.setListener();
        getViewDelegate().setRetryListener(this);
    }

    @Override
    protected void lazyLoad() {
        if (mInRefreshing) {
            return;
        }
        if (!NetworkUtil.isAvailable(this)) {
            refreshFailed(true, null);
            return;
        }
        refreshInit();
        loadRefreshDataAsync();
    }



    @Override
    public void onRetry(View retryView) {
        retryView.setOnClickListener(this);
    }

    @Override
    public void onEmpty(View emptyView) {
        emptyView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_empty_container:
                break;
            case R.id.ll_error_container:
                lazyLoad();
                break;
        }
    }

    /**
     * 刷新数据失败后的通用设置
     * @param isNetReason 说明该刷新失败是否网络错误导致
     */
    protected void refreshFailed(boolean isNetReason, Throwable e) {
        if (isNetReason) {
            getViewDelegate().showDefaultSnack("网络连接失败！");
        }
        if (e != null) {
            e.printStackTrace();
            AppDebugLog.e(AppDebugLog.TAG_DEBUG_INFO, e);
        }
        mInRefreshing = false;
        getViewDelegate().showError();
    }


    /**
     * 刷新数据成功后的通用设置
     */
    protected void refreshSuccess() {
        mInRefreshing = false;
        // 交由 bindData 去控制显示
//        getViewDelegate().showContent();
    }

    /**
     * 刷新数据时进行的初始化设置
     */
    protected void refreshInit() {
        mInRefreshing = true;
        getViewDelegate().showLoading();
    }

    /**
     * 重写该方法实现异步刷新数据逻辑，初始化设置请重写 {@link #refreshInit()}
     */
    protected abstract void loadRefreshDataAsync();

    @Override
    protected void bindData(@Nullable Data data) {
        refreshSuccess();
    }
}
