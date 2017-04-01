package com.jackiez.zgithub.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.jackiez.base.view.bingding.BaseBindingActivity;
import com.jackiez.zgithub.R;
import com.jackiez.zgithub.adapter.ToolbarSpinnerAdapter;
import com.jackiez.zgithub.databinding.ActivityMainBinding;
import com.jackiez.zgithub.databinding.ViewAcMainToolbarBinding;
import com.jackiez.zgithub.vm.MainVM;

/**
 * Created by zsigui on 16-10-25.
 */
public class MainActivity extends BaseBindingActivity<ActivityMainBinding, MainVM> implements AdapterView
        .OnItemSelectedListener {

    @Override
    protected MainVM createVM() {
        return new MainVM(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterSetContentView(@Nullable Bundle saveInstanceState) {
        super.afterSetContentView(saveInstanceState);
        setSupportActionBar(getBinding().toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ViewAcMainToolbarBinding barBinding = DataBindingUtil.inflate(LayoutInflater.from(getApplicationContext()),
                R.layout.view_ac_main_toolbar, getBinding().toolbar, false);
        barBinding.spDate.setAdapter(new ToolbarSpinnerAdapter());
        getBinding().toolbar.addView(barBinding.getRoot(),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        barBinding.spDate.setOnItemSelectedListener(this);

        getBinding().pager.setAdapter(new TapPagerAdapter(getSupportFragmentManager()));

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public static class TapPagerAdapter extends FragmentStatePagerAdapter {


        public TapPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
