package com.jackiez.base.widget.stackview;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;

/**
 * Created by zsigui on 17-4-1.
 */
public class StackView extends FrameLayout {

    private static final int DEFAULT_MAX_VISIBLE_VIEW_COUNT = 4;

    private StackTouchHelper mTouchHelper;
    private Adapter mAdapter;
    private View mCurrentTopView;

    /**
     * 进行视图缓存
     */
    private SparseArray<View> mCachedView;


    private int mCurrentTopIndex = 0;
    private boolean mIsTopView;
    /**
     * 当手势移除顶端的视图时，是否重新添加到栈底
     */
    private boolean mIsLoop;

    // 视图显示的内容
    private int mMaxVisibleViewCount = DEFAULT_MAX_VISIBLE_VIEW_COUNT;

    private int mRotationDegree;
    private int mScaleFactor;
    private int mTransformX;
    private int mTransformY;

    private boolean mDisableHardwareAcceleration = true;

    private DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mCurrentTopIndex = 0;
            mCurrentTopView = null;
            invalidate();
            requestLayout();
        }
    };

    public StackView(Context context) {
        this(context, null);
    }

    public StackView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchHelper = new StackTouchHelper(this);
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAdapter != null)
            mAdapter.registerDataSetObserver(mObserver);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAdapter != null)
            mAdapter.unregisterDataSetObserver(mObserver);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        if (mAdapter == null || mAdapter.getCount() == 0) {
            mCurrentTopView = null;
            mCurrentTopIndex = 0;
            removeAllViewsInLayout();
            mTouchHelper.unregisterObservable();
            return;
        }

        int itemCount = mAdapter.getCount();
        for (int i = getChildCount(); i < mMaxVisibleViewCount && mCurrentTopIndex < itemCount; i++) {
            View child = mAdapter.getView(0, getRecycledView(i), this);

            if (!mDisableHardwareAcceleration) {
                child.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }

            ViewGroup.LayoutParams defaultLp = child.getLayoutParams();
            if  (defaultLp == null) {
                defaultLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

            int measureSpecWidth = MeasureSpec.AT_MOST;
            int measureSpecHeight = MeasureSpec.AT_MOST;

            if (defaultLp.width == LayoutParams.MATCH_PARENT) {
                measureSpecWidth = MeasureSpec.EXACTLY;
            }
            if (defaultLp.height == LayoutParams.MATCH_PARENT) {
                measureSpecHeight = MeasureSpec.EXACTLY;
            }

            child.measure(measureSpecWidth | width, measureSpecHeight | height);
            addViewInLayout(child, 0, defaultLp, true);

            mCurrentTopIndex++;
        }

    }

    private View getRecycledView(int pos) {
        if (mCachedView == null) {
            return null;
        }
        int key = mAdapter.getItemViewType(pos);
        View cached = mCachedView.get(key);
        if (cached != null) {
            mCachedView.remove(key);
        }
        return cached;
    }

    public boolean isTopView(View v) {
        return mCurrentTopView != null && mCurrentTopView == v;
    }
}
