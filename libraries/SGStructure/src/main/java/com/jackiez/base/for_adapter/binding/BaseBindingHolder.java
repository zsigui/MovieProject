package com.jackiez.base.for_adapter.binding;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;

import com.jackiez.base.for_adapter.BaseHolder;

/**
 * Created by zsigui on 17-3-16.
 */

public abstract class BaseBindingHolder<Data, Binding extends ViewDataBinding> extends BaseHolder<Data> {

    private Binding mBinding;

    public BaseBindingHolder(@NonNull View itemView, @NonNull Binding binding) {
        super(itemView);
        mBinding = binding;
    }

    @NonNull
    public Binding getBinding() {
        return mBinding;
    }

    public void onViewRecycled() {}
}
