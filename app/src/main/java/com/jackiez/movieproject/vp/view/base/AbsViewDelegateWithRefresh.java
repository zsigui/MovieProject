package com.jackiez.movieproject.vp.view.base;

import android.content.Context;
import android.databinding.ViewDataBinding;

import com.jackiez.movieproject.R;
import com.jackiez.movieproject.views.widget.layout.RefreshLayout;

/**
 * Created by zsigui on 16-10-10.
 */

public abstract class AbsViewDelegateWithRefresh<Binding extends ViewDataBinding>
        extends AbsViewDelegateWithViewManager<Binding>  {

    private RefreshLayout mRefreshLayout;

    public AbsViewDelegateWithRefresh(Context context) {
        super(context);
        mRefreshLayout = (RefreshLayout) getRootView().findViewById(R.id.spl_container);
    }

    public void setLayoutRefreshing(boolean isRefresh) {
        if (isRefresh) {
            if (!mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(true);
            }
            mRefreshLayout.setEnabled(false);
        } else {
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.setEnabled(true);
        }
    }

    public void setLayoutLoading(int loadState) {
            mRefreshLayout.setLoadState(loadState);
    }

    public void setCanLoadMore(boolean canLoadMore) {
        mRefreshLayout.setCanShowFooter(canLoadMore);
    }
}
