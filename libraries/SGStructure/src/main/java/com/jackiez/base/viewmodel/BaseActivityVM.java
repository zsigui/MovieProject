package com.jackiez.base.viewmodel;

import android.databinding.BaseObservable;

import com.jackiez.base.view.bingding.BaseBindingActivity;

/**
 * 基础的ViewModel类，用于绑定更新Activity的视图，主要用于执行逻辑操作。 <br />
 * <br />
 * P.S. 此处通过泛型来处理类型转换，对于实际实例化类来说还是有较强的耦合，也可以参考 MVP 模式借助接口来实现
 *
 * Created by zsigui on 17-3-20.
 */
public abstract class BaseActivityVM<Activity extends BaseBindingActivity> extends BaseObservable {

    /**
     * ViewModel 持有该 Activity 以进行无法通过 Binding 更新数据和相关业务操作完成后的更新 UI 操作。
     */
    private Activity mActivity;

    public BaseActivityVM(Activity activity) {
        mActivity = activity;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public abstract void registerListener();

    public abstract void unregisterListener();
}
