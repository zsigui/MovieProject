package com.jackiez.movieproject.views.activity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jackiez.movieproject.views.listener.OnFinishListener;

import org.jetbrains.annotations.NotNull;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

public abstract class BaseActivity<Binding extends ViewDataBinding, Data> extends AppCompatActivity
        implements OnFinishListener {

    static final String STR_DATA = "com.jackiez.data";

    protected Binding mBinding;
    protected Data mData;

    protected boolean mHasInit = false;
    protected boolean mInDataLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        handleFirstOnCreate();
        super.onCreate(savedInstanceState);
        mBinding = initBinding();
        processLogic(savedInstanceState);
        setListener();
        mHasInit = false;
        if (mData == null) {
            lazyLoad();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mHasInit) {
            bindData(mData);
            mHasInit = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    @Override
    public void release() {
        mBinding = null;
        mData = null;
    }

    /**
     * 当有需要在 onCreate 开始执行的逻辑时重写此方法
     */
    protected void handleFirstOnCreate(){}

    /**
     * 重写此方法完成 Binding 的对象绑定
     * @return 返回不能为 null
     */
    @NotNull
    protected abstract Binding initBinding();

    /**
     * 执行初始化相关的操作逻辑
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * 在此方法中完成各个控件的监听
     */
    protected abstract void setListener();

    /**
     * 重写此方法进行数据的懒加载操作
     */
    protected abstract void lazyLoad();

    /**
     * 此方法中完成数据的绑定
     * @param data 传入数据，注意获取数据可能为 null
     */
    protected abstract void bindData(@Nullable Data data);
}
