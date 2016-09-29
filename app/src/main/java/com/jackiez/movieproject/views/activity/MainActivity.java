package com.jackiez.movieproject.views.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.jackiez.movieproject.R;
import com.jackiez.movieproject.databinding.ActivityMainBinding;
import com.jackiez.movieproject.model.entities.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding, List<Movie>>
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    @NotNull
    @Override
    protected ActivityMainBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        setSupportActionBar(mBinding.tbContainer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.dlContainer, mBinding.tbContainer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mBinding.dlContainer.setDrawerListener(toggle);
        toggle.syncState();

        mBinding.splContainer.setProgressViewOffset(true, -20, 100);
    }

    @Override
    protected void setListener() {
        mBinding.navView.setNavigationItemSelectedListener(this);
        mBinding.ablContainer.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mBinding.splContainer.setEnabled(verticalOffset >= 0);
            }
        });
        mBinding.splContainer.setOnRefreshListener(this);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void bindData(@Nullable List<Movie> movies) {

    }

    @Override
    public void onBackPressed() {
        if (mBinding.dlContainer.isDrawerOpen(GravityCompat.START)) {
            mBinding.dlContainer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        Snackbar.make(mBinding.clContainer, "侧边栏选项" + index + "被点击", Snackbar.LENGTH_SHORT).show();
        mBinding.dlContainer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        mBinding.splContainer.setRefreshing(false);
    }
}
