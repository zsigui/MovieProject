package com.jackiez.base.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.jackiez.base.listener.OnRetryListener;

/**
 * 实现Fragment类的基础逻辑
 * Created by zsigui on 17-3-9.
 */
public abstract class BaseFragment extends BaseFragment_Log implements OnRetryListener{

    private View mConvertView;

    private static final int COUNT_INDEX = 4;
    protected static final int INDEX_ID_LOADING = 0;
    protected static final int INDEX_ID_CONTENT = 1;
    protected static final int INDEX_ID_EMPTY = 2;
    protected static final int INDEX_ID_ERROR = 3;
    private static int[] ID_VIEWS = {R.id.sg_clpb_loading, R.id.sg_vs_content, R.id.sg_vs_empty, R.id.sg_vs_error};


    private boolean mCanRetryWhenError = false;
    private boolean mCanRetryWhenEmpty = false;
    private View[] mStateViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        if (mConvertView == null) {
            // 确保此处构建的视图只有较低的复杂度
            mConvertView = createConvertView(inflater, container);
        }
        return mConvertView;
    }

    /**
     * 当需要动态创建视图时才有必要重写该方法
     */
    protected View createConvertView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getConvertViewId(), container);
    }

    /**
     * 重写该方法提供XML视图ID，
     */
    @LayoutRes
    protected int getConvertViewId() {
        return R.layout.sg_fragment_main;
    }


    private void inflateContainerView(int index) {
        if (index == INDEX_ID_LOADING) {
            mStateViews[index] = mConvertView.findViewById(ID_VIEWS[index]);
        } else {
            mStateViews[index] = ((ViewStub) mConvertView.findViewById(ID_VIEWS[index])).inflate();

            if (mCanRetryWhenError && index == INDEX_ID_ERROR) {
                setCanRetryWhenError(true);
            }
            if (mCanRetryWhenEmpty && index == INDEX_ID_EMPTY) {
                setCanRetryWhenEmpty(true);
            }
        }
    }

    /**
     * 需要在 onCreateView 之后
     * @param index {@link #INDEX_ID_LOADING}、{@link #INDEX_ID_CONTENT}、{@link #INDEX_ID_EMPTY}、{@link #INDEX_ID_ERROR}
     */
    public void showView(int index) {
        if (mStateViews == null) {
            mStateViews = new View[COUNT_INDEX];
        }
        for (int i = 0; i < COUNT_INDEX; i++) {
            if (i == index) {
                if (mStateViews[i] == null) {
                    inflateContainerView(i);
                }
                if (!mStateViews[i].isShown()) {
                    mStateViews[i].setVisibility(View.VISIBLE);
                }
            } else {
                // 隐藏其他位置视图
                if (mStateViews[i] != null && mStateViews[i].isShown()) {
                    mStateViews[i].setVisibility(View.GONE);
                }
            }
        }
    }

    private void hideView(int index) {
        if (mStateViews != null && mStateViews[index] != null
                && mStateViews[index].isShown()) {
            mStateViews[index].setVisibility(View.GONE);
        }
    }

    /**
     * @param canRetryWhenError  true 支持错误页面点击回调 {@link #onRetryWhenError(View)}, false 不做处理
     */
    public void setCanRetryWhenError(boolean canRetryWhenError) {
        mCanRetryWhenError = canRetryWhenError;
        if (mCanRetryWhenError && mStateViews[INDEX_ID_ERROR] != null) {
            mStateViews[INDEX_ID_ERROR].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRetryWhenError(v);
                }
            });
        }
    }

    /**
     * @param canRetryWhenEmpty true 支持空页面点击回调 {@link #onRetryWhenEmpty(View)}, false 不做处理
     */
    public void setCanRetryWhenEmpty(boolean canRetryWhenEmpty) {
        mCanRetryWhenEmpty = canRetryWhenEmpty;
        if (mCanRetryWhenEmpty && mStateViews[INDEX_ID_EMPTY] != null) {
            mStateViews[INDEX_ID_EMPTY].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRetryWhenEmpty(v);
                }
            });
        }
    }

    /**
     * 当需要实现网络错误页面重试时重写该函数，并通过 {@link #setCanRetryWhenError(boolean)} 设置支持
     * @param v Error视图界面
     */
    @Override
    public void onRetryWhenError(View v) {
    }

    /**
     * 当需要实现网络错误页面重试时重写该函数，并通过 {@link #setCanRetryWhenError(boolean)} 设置支持
     *
     * @param v Empty视图界面
     */
    @Override
    public void onRetryWhenEmpty(View v) {}
}
