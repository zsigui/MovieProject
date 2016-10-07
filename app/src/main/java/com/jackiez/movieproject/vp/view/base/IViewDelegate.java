package com.jackiez.movieproject.vp.view.base;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.databinding.DataBindingComponent;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/3
 */

public interface IViewDelegate<Binding extends ViewDataBinding> {

    @NonNull Binding createBinding(LayoutInflater inflater, int layoutId, DataBindingComponent component);

    @LayoutRes int getLayoutId();

    Binding getBinding();
}
