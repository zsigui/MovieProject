package com.jackiez.movieproject.views.widget.layout;

/**
 * Created by zsigui on 16-1-11.
 */

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 *
 * @author mrsimple
 */
public class RefreshLayout extends SwipeRefreshLayout{

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
    private View mViewFooter;

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;
    /**
     * 是否显示加载更多
     */
    private boolean mCanShowLoad = true;

    /**
     * @param context
     */
    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        int id = context.getResources().getIdentifier("rl_view_footer", "layout", context.getPackageName());
        mViewFooter = LayoutInflater.from(context).inflate(id, null, false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化ListView对象
        if (mListView == null && mRecyclerView == null) {
            getListView();
        }
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
                        mListView.setOnScrollListener(new AbsListView.OnScrollListener(){
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {}

                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                                 int totalItemCount) {
                                // 滚动时到了最底部也可以加载更多
                                if (canLoad()) {
                                    loadData();
                                }
                            }
                        });
                        return;
                    } if (childView instanceof RecyclerView) {
                        mRecyclerView = (RecyclerView) childView;
                        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {}

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                mLastY = mYDown - dy;
                                if (canLoad()) {
                                    loadData();
                                }
                            }
                        });
                        return;
                    } else {
                        if (i == getChildCount() - 1) {
                            throw new IllegalStateException("RefreshLayout 需要缺少直接子类 ListView 或者 RecyclerView！");
                        }
                    }
                }
            }

        }
    }

    public boolean isCanShowLoad() {
        return mCanShowLoad;
    }

    public void setCanShowLoad(boolean canShowLoad) {
        mCanShowLoad = canShowLoad;
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
                mYDown = (int) ev.getY();
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
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     *
     * @return
     */
    private boolean canLoad() {
        return isCanShowLoad() && !isLoading && isBottom() && isPullUp();
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {
        if (mListView != null && mListView.getAdapter() != null) {
            return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
        } else if (mRecyclerView != null && mRecyclerView.getLayoutManager() != null) {
            View lastChildView = mRecyclerView.getLayoutManager()
                    .getChildAt(mRecyclerView.getLayoutManager().getChildCount() - 1);
            if (lastChildView != null) {
                int lastPosition = mRecyclerView.getLayoutManager().getPosition(lastChildView);
                if (lastPosition == mRecyclerView.getLayoutManager().getItemCount() - 1) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= TOUCH_SLOP;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
            setLoading(true);
            //
            mOnLoadListener.onLoadMore();
        }
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            if (mListView != null && mListView.getAdapter() != null) {
                if (mListView.getFooterViewsCount() == 0) {
                    mListView.addFooterView(mViewFooter);
                } else {
                    mViewFooter.setVisibility(View.VISIBLE);
                }

            } else if (mRecyclerView != null && mRecyclerView.getAdapter() != null
                    && mRecyclerView.getAdapter() instanceof FooterListener) {
                ((FooterListener) mRecyclerView.getAdapter()).showFooter(true);
            }
        } else {
            if (mListView != null && mListView.getAdapter() != null) {
                if (mListView.getAdapter() instanceof HeaderViewListAdapter) {
                    mListView.removeFooterView(mViewFooter);
                } else {
                    mViewFooter.setVisibility(View.GONE);
                }
            } else if (mRecyclerView != null && mRecyclerView.getAdapter() != null
                    && mRecyclerView.getAdapter() instanceof FooterListener) {
                ((FooterListener) mRecyclerView.getAdapter()).showFooter(false);
            }
            mYDown = 0;
            mLastY = 0;
        }
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadMoreListener loadListener) {
        mOnLoadListener = loadListener;
    }

    public interface FooterListener {
        void showFooter(boolean isShow);
    }

}

