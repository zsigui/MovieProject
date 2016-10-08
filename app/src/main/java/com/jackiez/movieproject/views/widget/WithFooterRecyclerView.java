package com.jackiez.movieproject.views.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jackiez.movieproject.views.widget.layout.OnLoadMoreListener;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/8
 */

public class WithFooterRecyclerView extends RecyclerView {

    private final static int TOUCH_SLOP = 50;

    private boolean isFooterShowing = false;
    private boolean mCanShowLoadMore = false;
    private View loadMoreView;
    private OnLoadMoreListener mOnLoadListener;
    private int mDownY;
    private int mLastY;

    public WithFooterRecyclerView(Context context) {
        this(context, null);
    }

    public WithFooterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WithFooterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        configureScroll();
    }

    private void configureScroll() {
        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    judgeLoad();
                }
            }

            private void judgeLoad() {
                if (canLoad()) {
                    if (mOnLoadListener != null) {
                        setLoading(true);
                        mOnLoadListener.onLoadMore();
                    }
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mLastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastY = (int) ev.getY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置当前加载状态
     */
    public void setLoading(boolean isLoading) {
        if (isFooterShowing == isLoading) {
            // 防止重复调用执行导致出错
            return;
        }
        isFooterShowing = isLoading;
        if (getAdapter() != null) {
            if (isFooterShowing) {
                getAdapter().notifyItemInserted(getAdapter().getItemCount() - 1);
            } else {
                getAdapter().notifyItemRemoved(getAdapter().getItemCount());
            }
        }
        mDownY = mLastY = 0;
    }

    private boolean canLoad() {
        return !isFooterShowing && mCanShowLoadMore && isBottom() && isPullUp();
    }

    private boolean isPullUp() {
        return mDownY - mLastY > TOUCH_SLOP;
    }

    private boolean isBottom() {
        if (getAdapter() == null) {
            // 没有设置Adapter，不进行判断
            Log.d("WithFooterRecyclerView", "Need to set Adapter before judging isBottom()");
            return false;
        }
        int lastItemPos = getAdapter().getItemCount() - 1;
        if (getLayoutManager() instanceof LinearLayoutManager
                || getLayoutManager() instanceof GridLayoutManager) {
            int lastVisiblePosition;
            if (getLayoutManager() instanceof LinearLayoutManager) {
                lastVisiblePosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
            } else {
                lastVisiblePosition = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
            }
            return lastVisiblePosition >= lastItemPos;

        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = ((StaggeredGridLayoutManager) getLayoutManager());
            int[] lastPoss = new int[layoutManager.getSpanCount()];
            layoutManager.findLastVisibleItemPositions(lastPoss);
            for (int pos : lastPoss) {
                if (pos >= lastItemPos) {
                    return true;
                }
            }
        } else {
            View lastChildView = getLayoutManager().findViewByPosition(lastItemPos);
            if (lastChildView != null) {
                return getLayoutManager().getPosition(lastChildView) >= lastItemPos;
            }
        }
        return false;
    }

    /**
     * 该状态用于决定是否显示执行加载更多项
     */
    public void setCanShowLoadMore(boolean canShowLoadMore) {
        mCanShowLoadMore = canShowLoadMore;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        LoadMoreAdapter loadMoreAdapter;
        if (adapter instanceof LoadMoreAdapter) {
            loadMoreAdapter = (LoadMoreAdapter) adapter;
        } else {
            loadMoreAdapter = new LoadMoreAdapter(adapter);
        }
        loadMoreAdapter.registerAdapterDataObserver(new AdapterDataObserver() {
        });
        adapter.notifyDataSetChanged();
        super.setAdapter(loadMoreAdapter);
    }

    public void setLoadMoreView(View loadMoreView) {
        this.loadMoreView = loadMoreView;
    }

    public void setLoadMoreView(@LayoutRes int layoutId) {
        this.loadMoreView = LayoutInflater.from(getContext()).inflate(layoutId, null, false);
    }

    public void setOnLoadListener(OnLoadMoreListener onLoadListener) {
        mOnLoadListener = onLoadListener;
    }

    private class LoadMoreHolder extends ViewHolder {

        LoadMoreHolder(View itemView) {
            super(itemView);
        }
    }

    public class LoadMoreAdapter extends RecyclerView.Adapter {

        private static final int TYPE_FOOTER = 0xF00F12AB;

        private RecyclerView.Adapter mAdapter;

        LoadMoreAdapter(Adapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);
            mAdapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            super.unregisterAdapterDataObserver(observer);
            mAdapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_FOOTER) {
                return new LoadMoreHolder(loadMoreView);
            }
            return mAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) != TYPE_FOOTER) {
                mAdapter.onBindViewHolder(holder, position);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return isFooterShowing && position == getItemCount() - 1 ?
                    TYPE_FOOTER : super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mAdapter.getItemCount() + (isFooterShowing ? 1 : 0);
        }
    }
}
