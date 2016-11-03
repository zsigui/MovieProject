package com.jackiez.movieproject.views.widget.layout;

/**
 * Created by zsigui on 16-1-11.
 */

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.jackiez.movieproject.R;

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 *
 * @author mrsimple
 */
public class RefreshLayout extends SwipeRefreshLayout {

    /**
     * 滑动到最下面时的上拉操作
     */

    private final static int TOUCH_SLOP = 50;
    /**
     * content view实例
     */
    private ListView mListView;
    private RecyclerView mRecyclerView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadMoreListener mOnLoadListener;

    /**
     * ListView的加载中footer
     */
    private View mViewFooterLoading;
    private View mViewFooterNoMore;
    private View mViewFooterError;

    private int mCurrentState = STATE_NONE;
    // 加载更多的几个状态
    /**
     * 当前不处于底部加载中，不显示FOOTER VIEW
     */
    public static final int STATE_NONE = 0;
    /**
     * 当前显示加载中
     */
    public static final int STATE_LOADING = 1;
    /**
     * 当前显示没有更多可以加载
     */
    public static final int STATE_NO_MORE = 2;
    /**
     * 当前提示加载更多时失败
     */
    public static final int STATE_ERROR = 3;

    /**
     * 按下时的y坐标
     */
    private int mDownY;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否显示加载更多
     */
    private boolean mCanShowFooter = true;


    /**
     * @param context
     */
    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化ListView对象
        if (mListView == null && mRecyclerView == null) {
            getListView();
        }
    }

    public void setViewFooterLoading(View view) {
        mViewFooterLoading = view;
    }

    public void setViewFooterLoading(@LayoutRes int layoutId) {
        setViewFooterLoading(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    public void setViewFooterNoMore(View view) {
        mViewFooterNoMore = view;
    }

    public void setViewFooterNoMore(@LayoutRes int layoutId) {
        setViewFooterNoMore(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    public void setViewFooterError(View view) {
        mViewFooterError = view;
    }

    public void setViewFooterError(@LayoutRes int layoutId) {
        setViewFooterError(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    private View getViewFooterLoading() {
        if (mViewFooterLoading == null) {
            setViewFooterLoading(R.layout.sg_default_footer_loading);
        }
        return mViewFooterLoading;
    }

    private View getViewFooterNoMore() {
        if (mViewFooterNoMore == null) {
            setViewFooterNoMore(R.layout.sg_default_footer_no_more);
        }
        return mViewFooterNoMore;
    }

    private View getViewFooterError() {
        if (mViewFooterError == null) {
            setViewFooterError(R.layout.sg_default_footer_error);
        }
        return mViewFooterError;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light);
        setProgressViewOffset(true, 20, 120);
    }

    /**
     * 获取List View对象
     */
    private void getListView() {

        int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View childView = getChildAt(i);
                if (childView != null) {
                    if (childView instanceof ListView) {
                        mListView = (ListView) childView;
                        // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {
                                if (scrollState == SCROLL_STATE_IDLE
                                        && canLoad()) {
                                    // 滚动时到了最底部也可以加载更多
                                    loadData();
                                }
                            }

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                                 int totalItemCount) {
                            }
                        });
                        return;
                    }
                    if (childView instanceof RecyclerView) {
                        mRecyclerView = (RecyclerView) childView;
                        wrapperRVAdapter(mRecyclerView);
                        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                if (newState == RecyclerView.SCROLL_STATE_IDLE
                                        && canLoad()) {
                                    loadData();
                                }
                            }
                        });
                        return;
                    }
                }
            }

        }
    }

    public boolean isCanShowFooter() {
        return mCanShowFooter;
    }

    public void setCanShowFooter(boolean canShowFooter) {
        mCanShowFooter = canShowFooter;
    }

    /*
         * (non-Javadoc)
         * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
         */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mLastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastY = (int) ev.getY();
                // 抬起
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否可以显示加载FooterView, 条件是到了最底部, 且非 {@link #STATE_NO_MORE} 或者 {@link #STATE_LOADING} 状态
     *
     * @return
     */
    private boolean canLoad() {
        return isCanShowFooter() && (mCurrentState != STATE_LOADING && mCurrentState != STATE_NO_MORE)
                && isBottom() && isPullUp();
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {
        if (mListView != null && mListView.getAdapter() != null) {
            return isListViewBottom();
        } else if (mRecyclerView != null && mRecyclerView.getLayoutManager() != null) {
            return isRecyclerViewBottom();
        }
        return false;
    }

    private boolean isRecyclerViewBottom() {
        wrapperRVAdapter(mRecyclerView);

        int lastItemPos = mRecyclerView.getAdapter().getItemCount() - 1;
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager
                || mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
            int lastVisiblePosition;
            if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                lastVisiblePosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
            } else {
                lastVisiblePosition = ((GridLayoutManager) mRecyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
            }
            return lastVisiblePosition >= lastItemPos;

        } else if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = ((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager());
            int[] lastPoss = new int[layoutManager.getSpanCount()];
            layoutManager.findLastVisibleItemPositions(lastPoss);
            for (int pos : lastPoss) {
                if (pos >= lastItemPos) {
                    return true;
                }
            }
        } else {
            View lastChildView = mRecyclerView.getLayoutManager().findViewByPosition(lastItemPos);
            if (lastChildView != null) {
                return mRecyclerView.getLayoutManager().getPosition(lastChildView) >= lastItemPos;
            }
        }
        return false;
    }

    private boolean isListViewBottom() {
        return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
    }

    /**
     * 判断并为RecyclerView的Adapter进行LoadMoreAdapter装饰
     */
    private void wrapperRVAdapter(RecyclerView recyclerView) {
        if (recyclerView != null && recyclerView.getAdapter() != null
                && !(recyclerView.getAdapter() instanceof LoadMoreAdapter)) {
            recyclerView.setAdapter(new LoadMoreAdapter(recyclerView.getAdapter()));
            // 注册相同监听器
            recyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {});
        }
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (mDownY - mLastY) >= TOUCH_SLOP;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
            setLoadState(STATE_LOADING);
            mOnLoadListener.onLoadMore();
        }
    }

    public void setLoadState(int state) {
        switch (state) {
            case STATE_ERROR:
            case STATE_NO_MORE:
            case STATE_LOADING:
            case STATE_NONE:
                break;
            default:
                throw new IllegalStateException("不支持的加载状态State，状态值: " + state);
        }
        if (mCurrentState == state) {
            // 状态没有变更，不重复处理
            return;
        }
        int lastState = mCurrentState;
        mCurrentState = state;
        if (mListView != null && mListView.getAdapter() != null) {
            setLVLoadState(state, lastState);
        } else if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
            setRVLoadState(state, lastState);
        }
    }

    private void setRVLoadState(int nowState, int lastState) {
        wrapperRVAdapter(mRecyclerView);
        if (nowState == STATE_NONE) {
            // lastState != nowState 的前提下，最新状态为 STATE_NONE，则移除 FOOT_VIEW
            mRecyclerView.getAdapter().notifyItemRemoved(mRecyclerView.getAdapter().getItemCount());
        } else {
            switch (lastState) {
                case STATE_NONE:
                    // lastState != nowState 的前提下，此前状态为 STATE_NONE，则需要添加 FOOT_VIEW
                    mRecyclerView.getAdapter().notifyItemInserted(mRecyclerView.getAdapter().getItemCount() - 1);
                    break;
                default:
                    // lastState != nowState 的前提下，此前状态为 非STATE_NONE，则需要变换 FOOT_VIEW
                    mRecyclerView.getAdapter().notifyItemChanged(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }
    }

    private void setLVLoadState(int nowState, int lastState) {
        if (nowState == STATE_NONE) {
            if (mListView.getFooterViewsCount() != 0) {
                // lastState != nowState 的前提下，最新状态为 STATE_NONE，则移除可能存在的 FOOT_VIEW
                if (!(mListView.getAdapter() instanceof HeaderViewListAdapter)) {
                    // 确保 ListAdapter 有封装为 HeaderViewListAdapter
                    addLVFootViewByState(lastState, mListView);
                }
                removeLVFootViewByState(lastState, mListView);
            }
        } else {
            if (mListView.getFooterViewsCount() != 0
                    && mListView.getAdapter() instanceof HeaderViewListAdapter) {
                removeLVFootViewByState(lastState, mListView);
            }
            addLVFootViewByState(nowState, mListView);
        }
        mCurrentState = nowState;
    }

    /**
     * 先判断此前的状态并清除对应FootView，该方法不对参数进行检查，调用前需保证安全
     */
    private void removeLVFootViewByState(int state, ListView listView) {
        switch (state) {
            case STATE_LOADING:
                listView.removeFooterView(getViewFooterLoading());
                break;
            case STATE_ERROR:
                listView.removeFooterView(getViewFooterError());
                break;
            case STATE_NO_MORE:
                listView.removeFooterView(getViewFooterNoMore());
                break;
        }
    }

    /**
     * 根据状态添加对应FootView，该方法不对参数进行检查
     */
    private void addLVFootViewByState(int state, ListView listView) {
        switch (state) {
            case STATE_LOADING:
                listView.addFooterView(getViewFooterLoading());
                break;
            case STATE_ERROR:
                listView.addFooterView(getViewFooterError());
                break;
            case STATE_NO_MORE:
                listView.addFooterView(getViewFooterNoMore());
                break;
        }
    }

    /**
     * 设置滑动到底部加载更多时的回调函数
     */
    public void setOnLoadListener(OnLoadMoreListener loadListener) {
        mOnLoadListener = loadListener;
    }

    /**
     * 加载更多适配器，用于封装 RecyclerView
     */
    private class LoadMoreAdapter extends RecyclerView.Adapter {

        private static final int TYPE_FOOTER_LOADING = 0xF00F12AB;
        private static final int TYPE_FOOTER_NO_MORE = 0xF00F12AC;
        private static final int TYPE_FOOTER_ERROR = 0xF00F12AD;

        private RecyclerView.Adapter mAdapter;

        LoadMoreAdapter(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            super.registerAdapterDataObserver(observer);
            mAdapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            super.unregisterAdapterDataObserver(observer);
            mAdapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_FOOTER_LOADING:
                    return getViewFooterLoading() == null ? null : new LoadMoreHolder(getViewFooterLoading());
                case TYPE_FOOTER_NO_MORE:
                    return getViewFooterNoMore() == null ? null : new LoadMoreHolder(getViewFooterNoMore());
                case TYPE_FOOTER_ERROR:
                    return getViewFooterError() == null ? null : new LoadMoreHolder(getViewFooterError());
            }
            return mAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type == TYPE_FOOTER_ERROR
                    || type == TYPE_FOOTER_LOADING
                    || type == TYPE_FOOTER_NO_MORE) {
                if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    LayoutParams lp = holder.itemView.getLayoutParams();
                    if (lp instanceof  StaggeredGridLayoutManager.LayoutParams) {
                        ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
                    }
                } else if (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    GridLayoutManager glm = (GridLayoutManager) mRecyclerView.getLayoutManager();
                    if (!(glm.getSpanSizeLookup() instanceof LoadMoreSpanSizeLookup)) {
                        glm.setSpanSizeLookup(new LoadMoreSpanSizeLookup(mRecyclerView));
                    }
                }
            } else {
                mAdapter.onBindViewHolder(holder, position);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mCurrentState != STATE_NONE && position == getItemCount() - 1) {
                switch (mCurrentState) {
                    case STATE_NO_MORE:
                        return TYPE_FOOTER_NO_MORE;
                    case STATE_ERROR:
                        return TYPE_FOOTER_ERROR;
                    case STATE_LOADING:
                        return TYPE_FOOTER_LOADING;
                    default:
                        throw new IllegalStateException("当前加载状态State错误，未识别值: " + mCurrentState);
                }
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mAdapter.getItemCount() + (mCurrentState != STATE_NONE ? 1 : 0);
        }
    }

    private static class LoadMoreSpanSizeLookup extends GridLayoutManager.SpanSizeLookup{

        private RecyclerView rv;

        public LoadMoreSpanSizeLookup(RecyclerView recyclerView) {
            rv = recyclerView;
        }

        @Override
        public int getSpanSize(int position) {
            if (rv == null) {
                return 0;
            }
            GridLayoutManager glm = (GridLayoutManager) rv.getLayoutManager();
            int type = rv.getAdapter().getItemViewType(position);
            if (type == LoadMoreAdapter.TYPE_FOOTER_ERROR
                    || type == LoadMoreAdapter.TYPE_FOOTER_LOADING
                    || type == LoadMoreAdapter.TYPE_FOOTER_NO_MORE) {
                return glm.getSpanCount();
            }
            return 1;
        }
    }

    /**
     * RecyclerView 底部加载更多视图 ViewHolder
     */
    private static class LoadMoreHolder extends RecyclerView.ViewHolder {

        private LoadMoreHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 加载更多接口
     */
    public interface OnLoadMoreListener {
        /**
         * 当加载更多时会调用该类
         */
        void onLoadMore();
    }
}

