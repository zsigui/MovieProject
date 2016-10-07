package com.jackiez.movieproject.vp.view.base;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/4
 */

public abstract class AbsViewDelegate<Binding extends ViewDataBinding> implements IViewDelegate<Binding> {

    protected Binding mBinding;

    public AbsViewDelegate(Context context) {
        createBinding(LayoutInflater.from(context), getLayoutId(), getDataBindingComponent());
    }

    @NonNull
    @Override
    public Binding createBinding(LayoutInflater inflater, int layoutId, DataBindingComponent component) {
        mBinding = DataBindingUtil.inflate(inflater, layoutId, null, false, component);
        return mBinding;
    }

    protected DataBindingComponent getDataBindingComponent(){
        return DataBindingUtil.getDefaultComponent();
    }

    @Override
    public Binding getBinding() {
        return mBinding;
    }

    public void showDefaultSnack(String msg) {
        Snackbar.make(getRootView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    public View getRootView() {
        return mBinding.getRoot();
    }

    /**
     * 获取根布局所附加的Activity，可能为 null
     */
    @SuppressWarnings("unchecked")
    public <T extends Activity> T getActivity() {
        return (T) getRootView().getContext();
    }

    public abstract void processLogic(Bundle saveInstanceState);

    public abstract void setListener();
}
