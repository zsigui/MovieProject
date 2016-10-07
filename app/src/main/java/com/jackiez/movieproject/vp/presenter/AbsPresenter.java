package com.jackiez.movieproject.vp.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jackiez.movieproject.vp.view.base.AbsViewDelegate;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/4
 */

public abstract class AbsPresenter<Data, ViewDelegate extends AbsViewDelegate> extends AppCompatActivity {

    private ViewDelegate mDelegate;
    protected Data mData;
    protected boolean mHasInit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewDelegate().getRootView());
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
        if (!mHasInit && mData != null) {
            bindData(mData);
            mHasInit = true;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected ViewDelegate getViewDelegate() {
        if (mDelegate == null) {
            synchronized (this) {
                if (mDelegate == null) {
                    try {
                        mDelegate = getDelegateClass().getConstructor(Context.class).newInstance(this);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mDelegate;
    }

    /**
     * 执行初始化相关的操作逻辑
     * @param savedInstanceState
     */
    protected void processLogic(Bundle savedInstanceState) {
        getViewDelegate().processLogic(savedInstanceState);
    }

    /**
     * 在此方法中完成各个控件的监听
     */
    protected void setListener() {
        getViewDelegate().setListener();
    }

    /**
     * 重写此方法进行数据的懒加载操作
     */
    protected abstract void lazyLoad();

    /**
     * 此方法中完成数据的绑定
     * @param data 传入数据，注意获取数据可能为 null
     */
    protected abstract void bindData(@Nullable Data data);

    protected abstract Class<ViewDelegate> getDelegateClass();
}
