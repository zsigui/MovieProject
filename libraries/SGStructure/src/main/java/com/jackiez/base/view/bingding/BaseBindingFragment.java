package com.jackiez.base.view.bingding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackiez.base.view.BaseFragment_Log;
import com.jackiez.base.viewmodel.BaseFragmentVM;

/**
 * Created by zsigui on 17-3-20.
 */

public abstract class BaseBindingFragment<B extends ViewDataBinding, VM extends BaseFragmentVM> extends BaseFragment_Log {
    private B mBinding;
    private VM mVModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        if (mBinding == null) {
            mBinding = createBinding(inflater, container);
        }
        if (mVModel == null) {
            mVModel = createVM();
        }
        return mBinding.getRoot();
    }

    protected abstract VM createVM();

    /**
     * 当需要配置自定义 binding 绑定组件时才有必要重写该方法
     */
    @NonNull
    protected B createBinding(LayoutInflater inflater, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, getConvertViewId(), container, false);
    }

    /**
     * 重写该方法提供XML视图ID
     */
    @LayoutRes
    protected abstract int getConvertViewId();

    public B getBinding() {
        return mBinding;
    }

    public VM getVModel() {
        return mVModel;
    }
}
