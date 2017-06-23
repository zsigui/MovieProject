package com.jackiez.base.widget.stackview;

import android.animation.Animator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;

/**
 * Created by zsigui on 17-4-1.
 */
public class StackView extends ViewGroup {

    private static final int ID_NEW_VIEW = 0x12455555;
    private static final int DEFAULT_ANIM_TIME = 200;
    private static final int DEFAULT_MAX_VISIBLE_VIEW_COUNT = 3;
    private static final int DEFAULT_LAYER_PADDING_X = 10;
    private static final int DEFAULT_LAYER_PADDING_Y = 10;
    private static final float DEFAULT_SCALE_FACTOR = 0.9f;
    private static final boolean DEFAULT_AVOID_OVERDRAW = true;
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
    private boolean mIsLoop = false;
    private boolean mForceReset = true;

    private boolean mIsFirstLayout = true;

    /**
     * 是否执行切割，避免多视图重绘，默认为 true
     */
    private boolean mIsAvoidOverdraw = DEFAULT_AVOID_OVERDRAW;

    // 视图显示的内容
    private int mMaxVisibleViewCount = DEFAULT_MAX_VISIBLE_VIEW_COUNT;

    private int mRotationDegree;
    private float mScaleFactor = DEFAULT_SCALE_FACTOR;
    private int mTransformX;
    private int mTransformY;

    private boolean mDisableHardwareAcceleration = true;

    private int mLayerSpaceX = DEFAULT_LAYER_PADDING_X;
    private int mLayerSpaceY = DEFAULT_LAYER_PADDING_Y;

    private ArrayList<RectF> mForClipRectFs;

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
        // 设置控件和子控件的绘制区域不受 padding 限制
        setClipToPadding(false);
        setClipChildren(false);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        if (mAdapter == null || mAdapter.getCount() == 0) {
            mCurrentTopView = null;
            mBottomVisibleViewIndex = 0;
            removeAllViewsInLayout();
            mTouchHelper.unregisterObservable();
            if (mForClipRectFs != null) {
                mForClipRectFs.clear();
            }
            return;
        }

        addNextItem();

        reMeasureChild();

        layoutChild();

        mIsFirstLayout = false;
        mForceReset = false;
    }

    private void reMeasureChild() {
        if (!mForceReset) {
            return;
        }
        Log.d("test-debug", "reMeasureChild");
        View child;
        int childCount = getChildCount();
        for (int layer = 0; layer < childCount; layer ++) {
            child = getChildAt(layer);

            // 层级计算，层级越高，放大倍数越小
            float scale = (float) Math.pow(mScaleFactor, childCount - layer - 1);

            int width = (int) (scale * (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()));
            int height = (int) (scale * (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()));

            LayoutParams lp = child.getLayoutParams();

            int measureSpecWidth = MeasureSpec.AT_MOST;
            int measureSpecHeight = MeasureSpec.AT_MOST;

            if (lp.width == LayoutParams.MATCH_PARENT) {
                measureSpecWidth = MeasureSpec.EXACTLY;
            }
            if (lp.height == LayoutParams.MATCH_PARENT) {
                measureSpecHeight = MeasureSpec.EXACTLY;
            }

            child.measure(measureSpecWidth | width, measureSpecHeight | height);
        }
    }

    private void layoutChild() {
        Log.d("test-debug", "layoutChild");
        int childCount = getChildCount();

        // 此处zOrder越大，说明层级越底（这是因为添加顺序造成的）
        if (childCount > 0) {
            mTouchHelper.unregisterObservable();
            mCurrentTopView = getChildAt(childCount - 1);
            mTouchHelper.registerObservable(mCurrentTopView);
        }


        if (mIsAvoidOverdraw) {
            if (mForClipRectFs == null) {
                mForClipRectFs = new ArrayList<>(childCount);
            }
            // 填补多余的空位
            for (int i = mForClipRectFs.size() - 1; i < childCount; i++) {
                mForClipRectFs.add(new RectF());
            }
        }

        View child;
        // 0, 1, 2, 3 -> l, r, t, b
        int[][] s = new int[childCount][4];
        for (int layer = childCount - 1; layer >= 0; layer--) {
            child = getChildAt(layer);
            if (layer == childCount - 1) {
                s[layer][0] = getPaddingLeft();
                s[layer][1] = s[layer][0] + child.getMeasuredWidth();
                s[layer][2] = getPaddingTop();
                s[layer][3] = s[layer][2] + child.getMeasuredHeight();
            } else {
                s[layer][1] = s[layer + 1][1] + mLayerSpaceX;
                s[layer][0] = s[layer][1] - child.getMeasuredWidth();
                s[layer][3] = s[layer + 1][3] + mLayerSpaceY;
                s[layer][2] = s[layer][3] - child.getMeasuredHeight();
            }
//            s[layer][2] = (getMeasuredHeight() - child.getMeasuredHeight()) >> 1;
//            s[layer][3] = s[layer][2] + child.getMeasuredHeight();

            // 将超出父视图的多余的部分切割掉
            if (s[layer][1] > getMeasuredWidth()) {
                s[layer][1] = getMeasuredWidth();
            }
            if (s[layer][3] > getMeasuredHeight())
                s[layer][3] = getMeasuredHeight();

            child.layout(s[layer][0], s[layer][2], s[layer][1], s[layer][3]);

            if (mIsAvoidOverdraw) {
                mForClipRectFs.get(layer).set(s[layer][0], s[layer][2], s[layer][1], s[layer][3]);
            }

//            boolean isNewView = (boolean) child.getTag(ID_NEW_VIEW);
//            if (isNewView) {
//                child.setTag(ID_NEW_VIEW, false);
//            }
        }
    }

    private void addNextItem() {
        Log.d("test-debug", "addNextItem");
        int itemCount = mAdapter.getCount();
        // 如果执行的是可以循环的
        int nextBottomVisibleViewIndex;
        Log.d("test-test", "addNextItem : " + getChildCount() + ", itemCount = " + itemCount);
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
        child.setTag(ID_NEW_VIEW, true);
        addViewInLayout(child, 0, defaultLp, true);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mIsAvoidOverdraw) {
            /*
            思路：
            1. 最顶层视图由于可以被触摸滑动，所以 canvas 不进行剪切，只对其下视图进行剪切
            2. 对非顶层视图绘制区域剪切包括(以A->B->C指代层级视图，以V指代整个canvas)：
                对于 B，有 B ∩ ( V - A )
                对于 C，有 C ∩ ( V - A ∪ B )
                对于 D，有 D ∩ ( V - A ∪ B ∪ C )

             P.S. 如何解决画布被切割后后续页面执行动画的问题呢？
             */
            View child;
            int childCount = getChildCount();
            if (childCount > 0) {
                int saveCount;
                // 设置 ClipRect 用于绘制时剔除 Overdraw 的部分
                for (int zOrder = 0; zOrder < childCount - 1; zOrder++) {
                    child = getChildAt(zOrder);
                    saveCount = canvas.save();
                    for (int k = zOrder + 1; k < childCount - 1; k++) {
                        canvas.clipRect(mForClipRectFs.get(k), Region.Op.DIFFERENCE);
                    }
                    canvas.clipRect(mForClipRectFs.get(zOrder), Region.Op.INTERSECT);
                    drawChild(canvas, child, getDrawingTime());
                    canvas.restoreToCount(saveCount);
                }

                // 绘制顶层，对于顶层由于需要执行拖曳等动作，故无须剪切显示区域
                saveCount = canvas.save();
                child = getChildAt(childCount - 1);
                drawChild(canvas, child, getDrawingTime());
                canvas.restoreToCount(saveCount);
            }
        } else {
            // 不避免重绘的情况下，直接调用父类方法即可
            super.dispatchDraw(canvas);
        }
    }

    public boolean isTopView(View v) {
        return mCurrentTopView != null && mCurrentTopView == v;
    }

    public void removeTopView() {
        if (mCurrentTopView != null) {
            Log.d("test-test", "removeTopView : " + mTopVisibleViewIndex);
            final float x = mCurrentTopView.getX();
            final float y = mCurrentTopView.getY();
            mCurrentTopView.animate()
                    .rotation(-10)
                    .alpha(0f)
                    .translationX(-mCurrentTopView.getWidth())
                    .translationY(mCurrentTopView.getY() - 10)
                    .setDuration(DEFAULT_ANIM_TIME)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            for (int i = getChildCount() - 2; i >= 0; i --) {
                                View self = getChildAt(i);
                                View old = getChildAt(i + 1);



                                float scaleX = ((float) old.getWidth()) / self.getWidth();
                                float scaleY = ((float) old.getHeight()) / self.getHeight();
                                Log.d("test-test", "x = " + old.getX() + ", y = " + old.getY() + ", sx = " + scaleX + ", sy = " + scaleY);
                                if (i == getChildCount() - 2) {
                                    self.animate()
                                            .x(x)
                                            .y(y)
                                            .setDuration(DEFAULT_ANIM_TIME)
                                            .start();
                                } else {
                                    self.animate()
                                            .x(old.getX())
                                            .y(old.getY())
                                            .setDuration(DEFAULT_ANIM_TIME)
                                            .start();
                                }
                            }


                            addViewForRecycled(mTopVisibleViewIndex, mCurrentTopView);
                            removeViewInLayout(mCurrentTopView);
                            mCurrentTopView = null;
                            mForceReset = true;
                            requestLayout();
                            invalidate();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

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
        invalidate();
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
