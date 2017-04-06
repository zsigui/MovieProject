package com.jackiez.base.widget.stackview;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
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

    private static final int ID_NEW_VIEW = 0x12455555;
    private static final int DEFAULT_ANIM_TIME = 200;
    private static final int DEFAULT_MAX_VISIBLE_VIEW_COUNT = 3;
    private static final int DEFAULT_LAYER_PADDING_X = 50;
    private static final int DEFAULT_LAYER_PADDING_Y = 50;
    /**
     * 因为{@link #mBottomVisibleViewIndex}和{@link #mTopVisibleViewIndex}都是指明当前视图栈中最顶或最低视图的下标，
     * 初始情况下无视图，故为-1
     */
    private static final int START_INDEX = -1;

    private StackTouchHelper mTouchHelper;
    private Adapter mAdapter;
    private View mCurrentTopView;

    /**
     * 进行视图缓存
     */
    private SparseArray<View> mCachedView;


    // 起始情况下，第一个可见视图下标等于最后一个可见视图下标
    private int mBottomVisibleViewIndex = START_INDEX;
    private int mTopVisibleViewIndex = START_INDEX;
    private boolean mIsTopView;
    /**
     * 当手势移除顶端的视图时，是否重新添加到栈底
     */
    private boolean mIsLoop;
    private boolean mForceReset;

    private boolean mIsFirstLayout = true;

    // 视图显示的内容
    private int mMaxVisibleViewCount = DEFAULT_MAX_VISIBLE_VIEW_COUNT;

    private int mRotationDegree;
    private float mScaleFactor = 0.9f;
    private int mTransformX;
    private int mTransformY;

    private boolean mDisableHardwareAcceleration = true;

    private int mLayerSpaceX = DEFAULT_LAYER_PADDING_X;
    private int mLayerSpaceY = DEFAULT_LAYER_PADDING_Y;

    private DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mBottomVisibleViewIndex = START_INDEX;
            mCurrentTopView = null;
            mForceReset = true;
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

    public void setAdapter(@NonNull Adapter adapter) {
        mAdapter = adapter;
//        mAdapter.registerDataSetObserver(mObserver);
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

//        if (!mForceReset || !changed)
//            return;

        if (mAdapter == null || mAdapter.getCount() == 0) {
            mCurrentTopView = null;
            mBottomVisibleViewIndex = 0;
            removeAllViewsInLayout();
            mTouchHelper.unregisterObservable();
            return;
        }

        addNextItem();

        int childCount = getChildCount();
        View child;
        int cL, cR, cT, cB;
        float scale;
        // 此处zOrder越大，说明层级越底（这是因为添加顺序造成的）
        int x;
        int y;
        if (childCount > 0) {
            mTouchHelper.unregisterObservable();
            mCurrentTopView = getChildAt(childCount - 1);
            mTouchHelper.registerObservable(mCurrentTopView);
        }
        for (int zOrder = 0; zOrder < childCount; zOrder++) {
            child = getChildAt(zOrder);

            scale = mScaleFactor * (100 - childCount * 6 + zOrder * 5) / 100.0f;

//            int width = (int) (child.getMeasuredWidth() * scale);
//            int height = (int) (child.getMeasuredHeight() * scale);

            int newPosY = mLayerSpaceX * (childCount - 1 - zOrder);
            int newPosX = mLayerSpaceX * (childCount - 1 - zOrder);


            cL = getPaddingLeft();
            cR = cL + child.getMeasuredWidth();

            cT = getPaddingTop();
            cB = cT + child.getMeasuredHeight();


            child.layout(cL, cT, cR, cB);

            if (mIsFirstLayout) {
                child.setTag(ID_NEW_VIEW, false);
                child.setScaleX(scale);
                child.setScaleY(scale);
                child.setY(newPosY);
                child.setX(newPosX);
            } else {
                boolean isNewView = (boolean) child.getTag(ID_NEW_VIEW);
                if (isNewView) {
                    child.setTag(ID_NEW_VIEW, false);
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                    child.setY(newPosY);
                    child.setX(newPosX);
                    child.setAlpha(0);
                }
                child.animate()
                        .scaleY(scale)
                        .scaleX(scale)
                        .y(newPosY)
                        .x(newPosX)
                        .alpha(1.0f)
                        .setDuration(DEFAULT_ANIM_TIME);
            }
        }

        mIsFirstLayout = false;
    }

    private void addNextItem() {
        int itemCount = mAdapter.getCount();
        // 如果执行的是可以循环的
        int nextBottomVisibleViewIndex;
        for (int i = getChildCount(); i < mMaxVisibleViewCount; i++) {
            nextBottomVisibleViewIndex = mBottomVisibleViewIndex + 1;
            if (nextBottomVisibleViewIndex == itemCount) {
                if (mIsLoop) {
                    // 循环情况下，到末尾时继续添加头
                    nextBottomVisibleViewIndex %= itemCount;
                } else {
                    // 没有循环的情况下，说明已经添加到底部了，无更多视图可以添加
                    return;
                }
            }
            if (nextBottomVisibleViewIndex == mTopVisibleViewIndex) {
                // 如果即将新添加的底部视图下标与顶部视图相同，说明现在已经处于循环中，
                // itemCount < mMaxVisibleViewCount，则不继续执行添加操作
                return;
            }

            addItemViewByPosition(nextBottomVisibleViewIndex);

            mBottomVisibleViewIndex = nextBottomVisibleViewIndex;
            if (i == 0) {
                // 当前视图栈中无视图，初始化下，故第一次Top=Bottom
                mTopVisibleViewIndex = mBottomVisibleViewIndex;
            }
        }
    }

    private void addItemViewByPosition(int pos) {
        View child = mAdapter.getView(pos, getRecycledView(pos), this);
        if (!mDisableHardwareAcceleration) {
            child.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        ViewGroup.LayoutParams defaultLp = child.getLayoutParams();
        if (defaultLp == null) {
            defaultLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
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
        child.setTag(ID_NEW_VIEW, true);
        addViewInLayout(child, 0, defaultLp, true);
    }

    public boolean isTopView(View v) {
        return mCurrentTopView != null && mCurrentTopView == v;
    }

    public void removeTopView() {
        if (mCurrentTopView != null) {
            addViewForRecycled(mTopVisibleViewIndex, mCurrentTopView);
            removeView(mCurrentTopView);
            mCurrentTopView = null;
            mForceReset = true;
        }
    }

    public void setMaxVisibleViewCount(int maxVisibleViewCount) {
        if (mMaxVisibleViewCount != maxVisibleViewCount) {
            mMaxVisibleViewCount = maxVisibleViewCount;
            mForceReset = true;
            requestLayout();
        }
    }

    public void resetStack() {
        mTopVisibleViewIndex = mBottomVisibleViewIndex = START_INDEX;
        mCurrentTopView = null;
        mForceReset = true;
        removeAllViewsInLayout();
        requestLayout();
    }

    private View getRecycledView(int pos) {
        if (mCachedView == null || mAdapter == null) {
            return null;
        }
        int key = mAdapter.getItemViewType(pos);
        View cached = mCachedView.get(key);
        if (cached != null) {
            mCachedView.remove(key);
        }
        return cached;
    }

    private void addViewForRecycled(int pos, View recycled) {
        if (recycled == null || mAdapter == null)
            return;
        if (mCachedView == null) {
            mCachedView = new SparseArray<>();
        }
        int key = mAdapter.getItemViewType(pos);
        mCachedView.put(key, recycled);
    }

    private void clearRecyled() {
        if (mCachedView != null) {
            mCachedView.clear();
        }
    }

}
