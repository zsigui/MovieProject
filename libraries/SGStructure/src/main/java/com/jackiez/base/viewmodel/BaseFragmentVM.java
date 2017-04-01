package com.jackiez.base.viewmodel;

import android.databinding.BaseObservable;
import android.support.v4.app.FragmentActivity;

import com.jackiez.base.view.bingding.BaseBindingFragment;

/**
 * Created by zsigui on 17-3-24.
 */
public abstract class BaseFragmentVM<Fragment extends BaseBindingFragment> extends BaseObservable {

    /**
     * ViewModel 持有该 Activity 以进行无法通过 Binding 更新数据和相关业务操作完成后的更新 UI 操作。
     */
    private Fragment mFragment;

    public BaseFragmentVM(Fragment fragment) {
        mFragment = fragment;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public FragmentActivity getActivity() { return mFragment != null ? mFragment.getActivity() : null; }

    public abstract void registerListener();

    public abstract void unregisterListener();
}