package com.jackiez.movieproject.views.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;

import com.jackiez.common.utils.Constants;
import com.jackiez.movieproject.R;
import com.jackiez.movieproject.model.entities.Movie;
import com.jackiez.movieproject.model.entities.PageData;
import com.jackiez.movieproject.model.rest.DataSource;
import com.jackiez.movieproject.utils.AppDebugLog;
import com.jackiez.movieproject.utils.NetworkUtil;
import com.jackiez.movieproject.utils.ObservableUtil;
import com.jackiez.movieproject.views.adapter.MainAdapter;
import com.jackiez.movieproject.views.adapter.decoration.RecyclerBoundDecoration;
import com.jackiez.movieproject.views.listener.OnFinishListener;
import com.jackiez.movieproject.vp.presenter.AbsPresenterWithViewManager;
import com.jackiez.movieproject.vp.view.MainActivityVD;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AbsPresenterWithViewManager<List<Movie>, MainActivityVD>
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener,
        OnFinishListener{

    private MainAdapter mAdapter;
    protected boolean mInRefreshing = false;
    protected boolean mInMoreLoading = false;

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        super.processLogic(savedInstanceState);

        mAdapter = new MainAdapter(this);
        getViewDelegate().initRecyclerView(mAdapter,
                new GridLayoutManager(this, 2),
                new RecyclerBoundDecoration(this)
        );
    }

    Subscription mRefreshSubscription;

    @Override
    protected void lazyLoad() {
        if (mInRefreshing) {
            return;
        }
        if (!NetworkUtil.isAvailable(this)) {
            refreshFailed(true);
            return;
        }
        refreshInit();
        ObservableUtil.unsubscribe(mRefreshSubscription);
        mRefreshSubscription = DataSource.getInstance().getMovieEngineApi()
                .getPopularMoviesByPage(Constants.API_KEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<PageData<Movie>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        AppDebugLog.e(AppDebugLog.TAG_DEBUG_INFO, e);
                        refreshFailed(false);
                        getViewDelegate().showDefaultSnack("数据加载失败!");
                    }

                    @Override
                    public void onNext(PageData<Movie> moviePageData) {
                        refreshSuccess();
                        bindData(moviePageData.results);
                    }
                });
    }

    @Override
    protected void bindData(@Nullable List<Movie> movies) {
        if (movies == null) {
            getViewDelegate().showError();
        } else if (movies.isEmpty()) {
            getViewDelegate().showEmpty();
        } else {
            getViewDelegate().showContent();
            mData = movies;
            mAdapter.updateData(mData);
        }
    }

    @Override
    protected Class<MainActivityVD> getDelegateClass() {
        return MainActivityVD.class;
    }

    @Override
    public void onBackPressed() {
        if (getViewDelegate().isDrawerOpen()) {
            getViewDelegate().closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        int index = -1;
        if (id == R.id.nav_camera) {
            // Handle the camera action
            index = 0;
        } else if (id == R.id.nav_gallery) {
            index = 1;

        } else if (id == R.id.nav_slideshow) {
            index = 2;

        } else if (id == R.id.nav_manage) {
            index = 3;

        } else if (id == R.id.nav_share) {
            index = 4;

        } else if (id == R.id.nav_send) {
            index = 5;

        }
        getViewDelegate().showDefaultSnack("侧边栏选项" + index + "被点击");
        getViewDelegate().closeDrawer();
        return true;
    }

    @Override
    public void onRefresh() {
        lazyLoad();
    }

    public static final int FIRST_PAGE = 1;
    protected int page = FIRST_PAGE;

    private void refreshFailed(boolean isNetReason) {
        if (isNetReason) {
            getViewDelegate().showDefaultSnack("网络连接失败！");
        }
        mInRefreshing = false;
        getViewDelegate().getBinding().splContainer.setRefreshing(false);
        getViewDelegate().showError();
    }

    private void refreshSuccess() {
        mInRefreshing = false;
        getViewDelegate().getBinding().splContainer.setRefreshing(false);
        // 交由 bindData 去控制显示
//        getViewDelegate().showContent();
        page = 1;
    }

    private void refreshInit() {
        mInRefreshing = true;
        if (mData == null || mData.isEmpty()) {
            getViewDelegate().showLoading();
        } else {
            if (!getViewDelegate().getBinding().splContainer.isRefreshing()) {
                getViewDelegate().getBinding().splContainer.setRefreshing(true);
            }
        }
    }

    private void loadMoreInit() {
        mInMoreLoading = true;
        getViewDelegate().getBinding().splContainer.setLoading(true);
    }

    private void loadMoreFailed(boolean isNetReason) {
        if (isNetReason) {
            getViewDelegate().showDefaultSnack("网络连接失败！");
        }
        mInMoreLoading = false;
        getViewDelegate().getBinding().splContainer.setLoading(false);
        getViewDelegate().showError();
    }

    private void loadMoreSuccess() {
        mInMoreLoading = false;
        page++;
        getViewDelegate().getBinding().splContainer.setLoading(true);
    }

    @Override
    public void release() {
        ObservableUtil.unsubscribe(mRefreshSubscription);
        mRefreshSubscription = null;
    }
}
