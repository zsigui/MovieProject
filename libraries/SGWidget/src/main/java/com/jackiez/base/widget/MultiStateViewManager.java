package com.jackiez.base.widget;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/1/2
 */
public class MultiStateViewManager {

    private static final int DEFAULT_NO_VIEW_ID = 0;
    private static int DEFAULT_LOAD_VIEW_ID = R.layout.sg_default_widget_loading;
    private static int DEFAULT_ERROR_RETRY_VIEW_ID = R.layout.sg_default_widget_error;
    private static int DEFAULT_EMPTY_VIEW_ID = R.layout.sg_default_widget_empty;

    /**
     * 设置默认全局加载视图资源ID
     */
    public static void setDefaultLoadViewId(int defaultLoadViewId) {
        if (defaultLoadViewId < 0) {
            return;
        }
        DEFAULT_LOAD_VIEW_ID = defaultLoadViewId;
    }

    /**
     * 设置默认全局访问出错重试视图资源ID
     */
    public static void setDefaultErrorRetryViewId(int defaultErrorRetryViewId) {
        if (defaultErrorRetryViewId < 0) {
            return;
        }
        DEFAULT_ERROR_RETRY_VIEW_ID = defaultErrorRetryViewId;
    }

    /**
     * 设置默认全局空视图资源ID
     */
    public static void setDefaultEmptyViewId(int defaultEmptyViewId) {
        if (defaultEmptyViewId < 0) {
            return;
        }
        DEFAULT_EMPTY_VIEW_ID = defaultEmptyViewId;
    }

    /**
     * 不同视图状态
     */
    private static final int TYPE_DEFAULT = -1;
    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_LOAD = 1;
    private static final int TYPE_ERROR_RETRY = 2;
    private static final int TYPE_EMPTY = 3;

    private ViewGroup mContainer;
    private Context mContext;

    private int mIdContentView;
    private int mIdLoadingView;
    private int mIdErrorRetryView;
    private int mIdEmptyView;

    private View mContentView;
    private View mLoadingView;
    private View mErrorRetryView;
    private View mEmptyView;
    private OnRetryOrEmptyListener mOnRetryListener;


    private int mLastType = TYPE_DEFAULT;


    private ShowTypeRunnable mRunnable;

    public static MultiStateViewManager generate(Context context) {
        return new MultiStateViewManager(context, null);
    }

    public static MultiStateViewManager generate(Context context, ViewGroup container) {
        return new MultiStateViewManager(context, container);
    }

    private MultiStateViewManager(Context context, ViewGroup container) {
        if (context == null) {
            Log.w("MultiStateViewManager", "Need a context to construct!");
            return;
        }
        mContext = context;
        if (container == null) {
            mContainer = new FrameLayout(context);
            mContainer.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            mContainer = container;
        }

        setContentView(DEFAULT_NO_VIEW_ID);
        setErrorRetryView(DEFAULT_ERROR_RETRY_VIEW_ID);
        setEmptyView(DEFAULT_EMPTY_VIEW_ID);
        setLoadView(DEFAULT_LOAD_VIEW_ID);
    }

    public View getContainer() {
        return mContainer;
    }

    public View getContentView() {
        if (mContentView == null && mIdContentView != DEFAULT_NO_VIEW_ID) {
            mContentView = inflateView(mIdContentView);
        }
        return mContentView;
    }

    public OnRetryOrEmptyListener getOnRetryOrEmptyListener() {
        return mOnRetryListener;
    }

    public void setOnRetryOrEmptyListener(OnRetryOrEmptyListener onRetryListener) {
        mOnRetryListener = onRetryListener;
    }

    public void showLoading() {
        showThread(TYPE_LOAD);
    }

    public void showErrorRetry() {
        showThread(TYPE_ERROR_RETRY);
    }


    public void showEmpty() {
        showThread(TYPE_EMPTY);
    }

    public void showContent() {
        showThread(TYPE_CONTENT);
    }

    public void showLast() {
        if (mContainer.getChildCount() != 0 && mContainer.getChildAt(0).isShown()
                && mContainer.getChildAt(0).getVisibility() == View.VISIBLE) {
            return;
        }
        showThread(mLastType);
    }

    public void setContentView(@LayoutRes int id) {
        if (id != DEFAULT_NO_VIEW_ID) {
            mIdContentView = id;
        }
    }

    public void setContentView(View v) {
        mContentView = v;
    }

    public void setEmptyView(@LayoutRes int id) {
        if (id != DEFAULT_NO_VIEW_ID) {
            mIdEmptyView = id;
        }
    }

    public void setLoadView(@LayoutRes int id) {
        if (id != DEFAULT_NO_VIEW_ID) {
            mIdLoadingView = id;
        }
    }

    public void setErrorRetryView(@LayoutRes int id) {
        if (id != DEFAULT_NO_VIEW_ID) {
            mIdErrorRetryView = id;
        }
    }

    public void setEmptyView(View v) {
        mEmptyView = v;
    }

    public void setErrorRetryView(View v) {
        mErrorRetryView = v;
    }

    public void setLoadView(View v) {
        mLoadingView = v;
    }

    private void showThread(final int type) {
        if (isMainThread()) {
            show(type);
        } else {
            if (mRunnable == null) {
                mRunnable = new ShowTypeRunnable();
            }
            mRunnable.setType(type);
            mContainer.post(mRunnable);
        }
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    private void show(int type) {
        if (mLastType != TYPE_DEFAULT
                && mLastType == type) {
            return;
        }
        // 防止重复请求过来
        mLastType = type;
        mContainer.removeAllViews();
        switch (type) {
            case TYPE_LOAD:
                if (mLoadingView == null && mIdLoadingView != DEFAULT_NO_VIEW_ID) {
                    mLoadingView = inflateView(mIdLoadingView);
                }
                if (mLoadingView != null) {
                    mContainer.addView(mLoadingView);
                    mLastType = TYPE_LOAD;
                } else {
                    Log.w("MultiStateViewManager", "No Loading View was Found to Show!" +
                            " Please Call setLoadingView() Before");
                }
                break;
            case TYPE_DEFAULT:
            case TYPE_CONTENT:
                if (mContentView == null && mIdContentView != DEFAULT_NO_VIEW_ID) {
                    mContentView = inflateView(mIdContentView);
                }
                if (mContentView != null) {
                    mContainer.addView(mContentView);
                    mLastType = TYPE_CONTENT;
                } else {
                    Log.w("MultiStateViewManager", "No Content View was Found to Show!" +
                            " Please Call setContentView() Before");
                }
                break;
            case TYPE_EMPTY:
                if (mEmptyView == null && mIdEmptyView != DEFAULT_NO_VIEW_ID) {
                    mEmptyView = inflateView(mIdEmptyView);
                }
                if (mEmptyView != null) {
                    mContainer.addView(mEmptyView);
                    mLastType = TYPE_EMPTY;
                    if (mOnRetryListener != null) {
                        mOnRetryListener.onEmpty(mEmptyView);
                    }
                } else {
                    Log.w("MultiStateViewManager", "No Empty View was Found to Show!" +
                            " Please Call setEmptyView() Before");
                }
                break;
            case TYPE_ERROR_RETRY:
                if (mErrorRetryView == null && mIdErrorRetryView != DEFAULT_NO_VIEW_ID) {
                    mErrorRetryView = inflateView(mIdErrorRetryView);
                }
                if (mErrorRetryView != null) {
                    mContainer.addView(mErrorRetryView);
                    mLastType = TYPE_ERROR_RETRY;
                    if (mOnRetryListener != null) {
                        mOnRetryListener.onRetry(mErrorRetryView);
                    }
                } else {
                    Log.w("MultiStateViewManager", "No Error View was Found to Show!" +
                            " Please Call setErrorView() Before");
                }
                break;
        }
        mContainer.invalidate();
    }

    private View inflateView(@LayoutRes int id) {
        return LayoutInflater.from(mContext).inflate(id, mContainer, false);
    }

    /**
     * 设置多布局的容器视图
     */
    public void setContainer(ViewGroup v) {
        mContainer = v;
    }

    /**
     * 进行重试回调接口
     */
    public interface OnRetryOrEmptyListener {
        void onRetry(View retryView);

        void onEmpty(View emptyView);
    }

    private class ShowTypeRunnable implements Runnable {

        private int type;

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            show(type);
        }
    }
}
