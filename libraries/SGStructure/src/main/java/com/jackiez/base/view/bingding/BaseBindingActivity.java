package com.jackiez.base.view.bingding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.jackiez.base.viewmodel.BaseActivityVM;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * 结合 DataBinding 实现的 View 层基础类，只用于实现跟界面显示相关的，跟业务相关的逻辑交由 ViewModel 实现
 *
 * Created by zsigui on 17-3-20.
 */
public abstract class BaseBindingActivity<B extends ViewDataBinding, VM extends BaseActivityVM> extends SupportActivity {

    B mBinding;
    VM mVModel;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getContentViewId(), getCustomBindingComponent());
        mVModel = createVM();
        afterSetContentView(savedInstanceState);
        registerListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterListener();
    }


    public void registerListener() {
        mVModel.registerListener();
    }

    public void unregisterListener() {
        mVModel.unregisterListener();
    }

    protected abstract VM createVM();

    /**
     * 当View存在自定义的绑定配置时，覆写该方法传递组件
     * @return 默认情况下返回 null
     */
    protected DataBindingComponent getCustomBindingComponent() {
        return null;
    }

    /**
     * 重写返回Activity的界面视图
     */
    @LayoutRes
    protected abstract int getContentViewId();

    /**
     * 该方法在 {@link #onCreate(Bundle)} 中调用，调用时机在 {@link #setContentView(int)} 之前，可在此方法中执行类似
     * 如 {@link #requestWindowFeature(int)}/{@link android.view.Window#setFlags(int, int)}/{@link #setTheme(int)}
     * 等操作逻辑
     */
    protected void beforeSetContentView(@Nullable Bundle savedInstanceState) {}

    /**
     * 该方法在 {@link #onCreate(Bundle)} 中调用，调用时机在 {@link #setContentView(int)} 之前
     */
    protected void afterSetContentView(@Nullable Bundle saveInstanceState) {}

    public B getBinding() {
        return mBinding;
    }

    public VM getVModel() {
        return mVModel;
    }
}
