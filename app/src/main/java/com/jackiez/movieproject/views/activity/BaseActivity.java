package com.jackiez.movieproject.views.activity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

public abstract class BaseActivity<Binding extends ViewDataBinding, Data> extends AppCompatActivity {

    static final String STR_DATA = "com.jackiez.data";

    private Binding mBinding;
    private Data mData;

    protected boolean mHasInit = false;
    protected boolean mInDataLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        handleFirstOnCreate();
        super.onCreate(savedInstanceState);
        initBinding();
        processLogic(savedInstanceState);
        setListener();
        mHasInit = false;
        if (mData == null) {
            lazyLoad();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mHasInit) {
            bindData(mData);
            mHasInit = true;
        }
    }

    protected void handleFirstOnCreate(){}

    @NotNull
    protected abstract Binding initBinding();

    protected abstract void processLogic(Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void lazyLoad();

    protected abstract void bindData(@Nullable Data data);
}
