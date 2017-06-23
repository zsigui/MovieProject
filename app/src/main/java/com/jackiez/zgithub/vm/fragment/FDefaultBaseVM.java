package com.jackiez.zgithub.vm.fragment;

import com.jackiez.base.rxbus2.RxBus;
import com.jackiez.base.viewmodel.BaseFragmentVM;
import com.jackiez.zgithub.view.fragment.DefaultBaseFragment;

/**
 * Created by zsigui on 17-3-27.
 */

public abstract class FDefaultBaseVM<F extends DefaultBaseFragment> extends BaseFragmentVM<F> {
    public FDefaultBaseVM(F fragment) {
        super(fragment);
    }

    @Override
    public void registerListener() {
        RxBus.get().register(this);
    }

    @Override
    public void unregisterListener() {
        RxBus.get().unregister(this);
    }

    public abstract void lazyLoad();

    public abstract void loadMore();
}
