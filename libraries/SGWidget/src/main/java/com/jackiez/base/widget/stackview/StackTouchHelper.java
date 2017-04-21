package com.jackiez.base.widget.stackview;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.jackiez.base.widget.stackview.listener.AnimProgressListener;

/**
 * Created by zsigui on 17-4-1.
 */

public class StackTouchHelper implements View.OnTouchListener, AnimProgressListener{

    private StackView mRootView;
    private AnimProgressListener mAnimProgressListener;
    private View mLastObservable;
    private ViewPropertyAnimator mResetAnimator;


    private int mPointerId;
    private float mDownX;
    private float mDownY;

    // 记录初始的状态，便于恢复
    private float mInitX;
    private float mInitY;
    private boolean mNeedRecordFirstLocation;

    public StackTouchHelper(StackView stackView) {
        checkIsNotNull(stackView);
        mRootView = stackView;
    }

    private void checkIsNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Not allowed null object here!");
        }
    }


    /**
     * 将 StackView 顶端视图的触摸处理事件交由本 Helper 进行处理
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 对于当前非处于顶端的视图，将事件消费掉但不进行处理
        if (!mRootView.isTopView(v))
            return true;

        if (mResetAnimator != null) {
            mResetAnimator.cancel();
            mResetAnimator = null;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mRootView.getParent().requestDisallowInterceptTouchEvent(true);
                mPointerId = event.getPointerId(0);
                mDownX = event.getX();
                mDownY = event.getY();
                if (mNeedRecordFirstLocation) {
                    mInitX = mLastObservable.getX();
                    mInitY = mLastObservable.getY();
                    mNeedRecordFirstLocation = false;
                }
                Log.d("test-test", "down x = " + mDownX + ", y = " + mDownY + ", x = " + mLastObservable.getX() + ", y = " + mLastObservable.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                float progress = 0.0f;
                if (mAnimProgressListener == null || !mAnimProgressListener.onProgressUpdate(v, progress)) {
                    onProgressUpdate(v, progress);
                }
                int pointerIndex = event.findPointerIndex(mPointerId);
                if (pointerIndex < 0) return false;
                float dx = event.getX() - mDownX;
                float dy = event.getY() - mDownY;
                float newX = mLastObservable.getX() + dx;
                float newY = mLastObservable.getY() + dy;
                mLastObservable.setX(newX);
                mLastObservable.setY(newY);
                return true;
            case MotionEvent.ACTION_UP:
                mRootView.getParent().requestDisallowInterceptTouchEvent(false);
                if (mResetAnimator != null) {
                    mResetAnimator.cancel();
                }
                mResetAnimator = mLastObservable.animate();
                Log.d("test-test", "init x = " + mInitX + ", y = " + mInitY);
                mResetAnimator.x(mInitX);
                mResetAnimator.y(mInitY);
                mResetAnimator.setDuration(200);
                mResetAnimator.start();
                return true;
        }

        return false;
    }

    /**
     * 实现该方法完成默认拖曳位置视图的变化
     *
     */
    @Override
    public boolean onProgressUpdate(View v, float p) {
        return false;
    }

    public void unregisterObservable() {
        if (mLastObservable != null) {
            mLastObservable.setOnTouchListener(null);
        }
    }

    public void registerObservable(View v) {
        if (v == null)
            return;
        mLastObservable = v;
        mNeedRecordFirstLocation = true;
        mLastObservable.setOnTouchListener(this);
    }
}
