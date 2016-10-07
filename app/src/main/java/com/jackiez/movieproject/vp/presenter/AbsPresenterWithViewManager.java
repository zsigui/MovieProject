package com.jackiez.movieproject.vp.presenter;

import android.os.Bundle;

import com.jackiez.movieproject.vp.view.base.AbsViewDelegateWithViewManager;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/4
 */

public abstract class AbsPresenterWithViewManager<Data, ViewDelegate extends AbsViewDelegateWithViewManager>
        extends AbsPresenter<Data, ViewDelegate> {

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        super.processLogic(savedInstanceState);
    }

    @Override
    protected void lazyLoad() {
        getViewDelegate().showLoading();
    }
}
