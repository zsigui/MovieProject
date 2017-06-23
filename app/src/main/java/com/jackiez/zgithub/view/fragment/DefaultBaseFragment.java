package com.jackiez.zgithub.view.fragment;

import android.databinding.ViewStubProxy;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewStub;

import com.jackiez.base.view.bingding.BaseBindingFragment;
import com.jackiez.zgithub.R;
import com.jackiez.zgithub.databinding.FragmentBaseDefaultBinding;
import com.jackiez.zgithub.util.ViewUtil;
import com.jackiez.zgithub.vm.fragment.FDefaultBaseVM;

/**
 * Created by zsigui on 17-3-27.
 */

public abstract class DefaultBaseFragment<VM extends FDefaultBaseVM>
        extends BaseBindingFragment<FragmentBaseDefaultBinding, VM>
        implements View.OnClickListener {

    private static final int COUNT_INDEX = 4;
    protected static final int INDEX_ID_LOADING = 0;
    protected static final int INDEX_ID_CONTENT = 1;
    protected static final int INDEX_ID_EMPTY = 2;
    protected static final int INDEX_ID_ERROR = 3;
    private View[] mStateViews;

    /**
     * 获取用于Fragment视图创建的布局ID
     */
    @Override
    protected int getConvertViewId() {
        return R.layout.fragment_base_default;
    }

    /**
     * 获取用于填充错误页面的布局ID
     */
    @LayoutRes
    public int getErrorLayoutId() {
        return R.layout.fragment_base_vs_error;
    }

    /**
     * 获取用于填充空数据页面的布局ID
     */
    @LayoutRes
    public int getEmptyLayoutId() {
        return R.layout.fragment_base_vs_empty;
    }

    /**
     * 获取用于填充实际显示内容页面的布局ID
     */
    @LayoutRes
    public abstract int getContentLayoutId();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getVModel().registerListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getVModel().unregisterListener();
    }

    private void inflateContainerView(int index) {
        switch (index) {
            case INDEX_ID_LOADING:
                mStateViews[index] = getBinding().clpbLoading;
                break;
            case INDEX_ID_CONTENT:
                inflateViewStub(getBinding().vsContent, index, getContentLayoutId());
                break;
            case INDEX_ID_EMPTY:
                inflateViewStub(getBinding().vsEmpty, index, getEmptyLayoutId());
                break;
            case INDEX_ID_ERROR:
                inflateViewStub(getBinding().vsError, index, getErrorLayoutId());
                break;
        }
    }

    private void inflateViewStub(ViewStubProxy vsp, int index, @LayoutRes int layoutId) {
        if (!vsp.isInflated()) {
            vsp.getViewStub().setLayoutResource(layoutId);
            final int i = index;
            vsp.setOnInflateListener(new ViewStub.OnInflateListener() {
                @Override
                public void onInflate(ViewStub stub, View inflated) {
                    mStateViews[i] = inflated;
                    showView(i);
                    inflateCallback(i);
                }
            });
            vsp.getViewStub().inflate();
        } else {
            mStateViews[index] = vsp.getRoot();
        }
    }

    /**
     * ViewStub 视图填充完成后回调该方法，重写该方法完成初始化
     *
     * @param index 当前填充的视图下标
     */
    protected void inflateCallback(int index) {
        switch (index) {
            case INDEX_ID_CONTENT:

                break;
            case INDEX_ID_ERROR:
                View v = mStateViews[index];
                ViewUtil.findViewById(v, R.id.btn_retry)
                        .setOnClickListener(this);
                break;
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        showLoading();
        getVModel().lazyLoad();
    }

    /**
     * 需要在 onCreateView 之后
     * @param index {@link #INDEX_ID_LOADING}、{@link #INDEX_ID_CONTENT}、{@link #INDEX_ID_EMPTY}、{@link #INDEX_ID_ERROR}
     */
    protected final void showView(int index) {
        if (mStateViews == null) {
            mStateViews = new View[COUNT_INDEX];
            inflateContainerView(INDEX_ID_LOADING);
        }
        for (int i = 0; i < COUNT_INDEX; i++) {
            if (i == index) {
                if (mStateViews[i] == null) {
                    inflateContainerView(i);
                }
                if (mStateViews[i] != null && mStateViews[i].getVisibility() != View.VISIBLE) {
                    mStateViews[i].setVisibility(View.VISIBLE);
                }
            } else {
                // 隐藏其他位置视图
                hideView(i);
            }
        }
    }

    protected final void hideView(int index) {
        if (mStateViews != null && mStateViews[index] != null
                && mStateViews[index].getVisibility() != View.GONE) {
            mStateViews[index].setVisibility(View.GONE);
        }
    }

    public void showError() {
        showView(INDEX_ID_ERROR);
    }

    public void showContent() {
        showView(INDEX_ID_CONTENT);
    }

    public void showLoading() {
        showView(INDEX_ID_LOADING);
    }

    public void showEmpty() {
        showView(INDEX_ID_EMPTY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_retry:
                onLazyInitView(getArguments());
                break;
        }
    }
}
