package com.jackiez.movieproject.vp.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.jackiez.movieproject.R;
import com.jackiez.movieproject.databinding.ActivityMainBinding;
import com.jackiez.movieproject.utils.UIUtil;
import com.jackiez.movieproject.views.activity.MainActivity;
import com.jackiez.movieproject.views.adapter.MainAdapter;
import com.jackiez.movieproject.vp.view.base.AbsViewDelegateWithRefresh;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/4
 */

public class MainActivityVD extends AbsViewDelegateWithRefresh<ActivityMainBinding> {

    public MainActivityVD(Context context) {
        super(context);
        mViewManager.setContainer(mBinding.flContainer);
        mViewManager.setContentView(mBinding.splContainer);
    }

    @Override
    public void processLogic(Bundle saveInstanceState) {
        MainActivity activity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            UIUtil.setTranslucentStatusBar(activity);
        }
        activity.setSupportActionBar(mBinding.tbContainer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this.getActivity(), mBinding.dlContainer, mBinding.tbContainer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.dlContainer.setDrawerListener(toggle);
        toggle.syncState();
        mBinding.splContainer.setProgressViewOffset(true, -20, 120);
        mBinding.splContainer.setEnabled(false);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mBinding.tbContainer.getLayoutParams();
        lp.topMargin = UIUtil.getStatusBarSize(activity);
        mBinding.tbContainer.setLayoutParams(lp);

    }

    @Override
    public void setListener() {
        MainActivity activity = getActivity();
        mBinding.navView.setNavigationItemSelectedListener(activity);
        mBinding.ablContainer.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mBinding.splContainer.setEnabled(verticalOffset >= 0);
            }
        });
        mBinding.splContainer.setOnRefreshListener(activity);
        mBinding.splContainer.setOnLoadListener(activity);
    }

    @Override
    @LayoutRes
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public View getRootView() {
        return mBinding.getRoot();
    }

    public void openDrawer() {
        if (!mBinding.dlContainer.isDrawerOpen(GravityCompat.START)) {
            mBinding.dlContainer.openDrawer(GravityCompat.START, true);
        }
    }

    public void closeDrawer() {
        if (mBinding.dlContainer.isDrawerOpen(GravityCompat.START)) {
            mBinding.dlContainer.closeDrawer(GravityCompat.START);
        }
    }

    public boolean isDrawerOpen() {
        return mBinding.dlContainer.isDrawerOpen(GravityCompat.START);
    }

    public void initRecyclerView(MainAdapter adapter, RecyclerView.LayoutManager layoutManager,
                                 RecyclerView.ItemDecoration... decorations) {
        if (decorations != null) {
            for (RecyclerView.ItemDecoration item : decorations) {
                mBinding.rvContent.addItemDecoration(item);
            }
        }
        mBinding.rvContent.setLayoutManager(layoutManager);
        mBinding.rvContent.setAdapter(adapter);
    }
}
