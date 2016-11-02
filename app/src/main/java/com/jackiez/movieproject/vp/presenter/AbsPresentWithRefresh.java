package com.jackiez.movieproject.vp.presenter;

import android.support.annotation.Nullable;

import com.jackiez.movieproject.utils.AppDebugLog;
import com.jackiez.movieproject.utils.NetworkUtil;
import com.jackiez.movieproject.views.widget.layout.RefreshLayout;
import com.jackiez.movieproject.vp.view.base.AbsViewDelegateWithRefresh;

/**
 * Created by zsigui on 16-10-10.
 */

public abstract class AbsPresentWithRefresh<Data, ViewDelegate extends AbsViewDelegateWithRefresh>
        extends AbsPresenterWithViewManager<Data, ViewDelegate> implements RefreshLayout.OnRefreshListener,
        RefreshLayout.OnLoadMoreListener{

    protected boolean mInMoreLoading = false;
    public static final int FIRST_PAGE = 1;
    protected int page = FIRST_PAGE;

    protected void refreshFailed(boolean isNetReason, Throwable e) {
        super.refreshFailed(isNetReason, e);
        getViewDelegate().setLayoutRefreshing(false);
    }

    protected void refreshSuccess() {
        super.refreshSuccess();
        getViewDelegate().setLayoutRefreshing(false);
        page++;
    }

    protected void refreshInit() {
        mInRefreshing = true;
        if (mData == null) {
            getViewDelegate().showLoading();
        } else {
            getViewDelegate().setLayoutRefreshing(true);
        }
    }

    protected void loadMoreInit() {
        mInMoreLoading = true;
        getViewDelegate().setLayoutLoading(RefreshLayout.STATE_LOADING);
    }

    protected void loadMoreFailed(boolean isNetReason, Throwable e) {
//        if (isNetReason) {
//            getViewDelegate().showDefaultSnack("网络连接失败！");
//        } else {
//            getViewDelegate().showDefaultSnack("获取更多数据失败！");
//        }
        if (e != null) {
            e.printStackTrace();
            AppDebugLog.e(AppDebugLog.TAG_DEBUG_INFO, e);
        }
        mInMoreLoading = false;
        getViewDelegate().setLayoutLoading(RefreshLayout.STATE_ERROR);
    }

    protected void loadMoreSuccess() {
        mInMoreLoading = false;
        page++;
        getViewDelegate().setLayoutLoading(RefreshLayout.STATE_NONE);
    }

    @Override
    public void onRefresh() {
        lazyLoad();
    }

    @Override
    public void onLoadMore() {
        if (mInMoreLoading) {
            return;
        }
        if (!NetworkUtil.isAvailable(this)) {
            loadMoreFailed(true, null);
            return;
        }
        loadMoreInit();
        loadLoadMoreDataAsync();
    }

    protected abstract void loadLoadMoreDataAsync();

    protected abstract void addMoreData(@Nullable Data data);
}
