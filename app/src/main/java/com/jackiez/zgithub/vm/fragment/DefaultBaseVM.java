package com.jackiez.zgithub.vm.fragment;

import com.jackiez.base.viewmodel.BaseFragmentVM;
import com.jackiez.zgithub.view.fragment.DefaultBaseFragment;

/**
 * Created by zsigui on 17-3-27.
 */

public abstract class DefaultBaseVM<F extends DefaultBaseFragment> extends BaseFragmentVM<F> {
    public DefaultBaseVM(F fragment) {
        super(fragment);
    }

    @Override
    public void registerListener() {

    }

    @Override
    public void unregisterListener() {

    }

    public abstract void lazyLoad();

    public abstract void loadMore();
}
