package com.jackiez.movieproject.views.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.jackiez.common.utils.Constants;
import com.jackiez.common.utils.KeyConst;
import com.jackiez.movieproject.R;
import com.jackiez.movieproject.common.GlobalCache;
import com.jackiez.movieproject.model.entities.Movie;
import com.jackiez.movieproject.model.entities.PageData;
import com.jackiez.movieproject.model.rest.DataSource;
import com.jackiez.movieproject.utils.AppDebugLog;
import com.jackiez.movieproject.utils.ObservableUtil;
import com.jackiez.movieproject.utils.UIUtil;
import com.jackiez.movieproject.views.adapter.MainAdapter;
import com.jackiez.movieproject.views.adapter.decoration.RecyclerBoundDecoration;
import com.jackiez.movieproject.views.listener.OnFinishListener;
import com.jackiez.movieproject.views.listener.OnItemClickListener;
import com.jackiez.movieproject.vp.presenter.AbsPresentWithRefresh;
import com.jackiez.movieproject.vp.view.MainActivityVD;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AbsPresentWithRefresh<List<Movie>, MainActivityVD>
        implements NavigationView.OnNavigationItemSelectedListener, OnFinishListener, OnItemClickListener<Movie> {

    private MainAdapter mAdapter;
    private Subscription mRefreshSubscription;
    private Subscription mLoadMoreSubscription;

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        UIUtil.setTranslucentStatusBar(this);
        super.processLogic(savedInstanceState);

        mAdapter = new MainAdapter(this);
        getViewDelegate().initRecyclerView(mAdapter,
                new GridLayoutManager(this, 2),
                new RecyclerBoundDecoration(this)
        );
    }

    @Override
    protected void setListener() {
        super.setListener();
        mAdapter.setItemClickListener(this);
    }

    @Override
    protected void loadRefreshDataAsync() {
        ObservableUtil.unsubscribe(mRefreshSubscription);
        mRefreshSubscription = DataSource.getInstance().getMovieEngineApi()
                .getPopularMoviesByPage(Constants.API_KEY, FIRST_PAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<PageData<Movie>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshFailed(false, e);
//                        getViewDelegate().showDefaultSnack("数据加载失败!");

                    }

                    @Override
                    public void onNext(PageData<Movie> moviePageData) {
                        refreshSuccess();
                        bindData(moviePageData.results);
                    }
                });
    }


    @Override
    protected void loadLoadMoreDataAsync() {
        ObservableUtil.unsubscribe(mLoadMoreSubscription);
        mLoadMoreSubscription = DataSource.getInstance().getMovieEngineApi()
                .getPopularMoviesByPage(Constants.API_KEY, page)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<PageData<Movie>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadMoreFailed(false, e);
                    }

                    @Override
                    public void onNext(PageData<Movie> moviePageData) {
                        loadMoreSuccess();
                        addMoreData(moviePageData.results);
                    }
                });
    }

    @Override
    protected void addMoreData(@Nullable List<Movie> movies) {
        mAdapter.addMoreData(movies);
        mData = mAdapter.getData();
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
    public void onClick(View v) {
        AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "MainActivity.onClick()");
        super.onClick(v);
    }

    @Override
    public void release() {
        ObservableUtil.unsubscribe(mRefreshSubscription);
        ObservableUtil.unsubscribe(mLoadMoreSubscription);
        mRefreshSubscription = null;
    }

    @Override
    public void itemClick(View v, int position, Movie item) {

        final Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(KeyConst.DATA, item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String shareKey = "share" + position;
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, v, shareKey
            );
            intent.putExtra(KeyConst.KEY, shareKey);
            Drawable b = ((ImageView) v).getDrawable();
            GlobalCache.sPhotoCache = new WeakReference<>(b);
            startActivity(intent, optionsCompat.toBundle());
        } else {
            v.animate().scaleY(0).scaleX(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startActivity(intent);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }
}
