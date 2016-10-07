package com.jackiez.movieproject.views.adapter.base;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/6
 */

public abstract class BaseHolder<Binding extends ViewDataBinding> extends RecyclerView.ViewHolder{

    protected Binding mBinding;

    public BaseHolder(Binding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }
}
