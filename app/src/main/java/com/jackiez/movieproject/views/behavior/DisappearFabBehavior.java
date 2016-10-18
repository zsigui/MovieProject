package com.jackiez.movieproject.views.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.jackiez.movieproject.utils.AppDebugLog;
import com.jackiez.movieproject.utils.UIUtil;
import com.jackiez.movieproject.views.widget.LobsterTextView;

/**
 * Created by zsigui on 16-10-17.
 */

public class DisappearFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {


    private int mStatusBarSize;
    private int mTotalDistance;

    public DisappearFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof LobsterTextView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency.getParent() == null || !(dependency.getParent() instanceof ViewGroup)) {
            return false;
        }
        if (child.getTag() != null) {
            return false;
        }
        ViewGroup vg = (ViewGroup) dependency.getParent();

//        View v = vg.findViewById(R.id.v_tool);
        initConf(child, dependency, vg);

        float curDiffTop = mTotalDistance + vg.getTop(); // getTop() 为负数
        float fade;
        if (curDiffTop >= 2 * mStatusBarSize) {
            fade = 1;
        } else if (curDiffTop <= mStatusBarSize) {
            fade = 0;
        } else {
            fade = (curDiffTop - mStatusBarSize) / (mStatusBarSize);
        }
        child.setScaleX(fade);
        child.setScaleY(fade);

        AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "getTop=" + dependency.getHeight());
//        if (curDiffTop <= mStatusBarSize) {
//            dependency.setPadding(0, mStatusBarSize, 0, 0);
//        } else {
//            dependency.setPadding(0, 0, 0, 0);
//        }
        return true;
    }

    private void initConf(FloatingActionButton child, View dependency, ViewGroup parent) {
        if (mStatusBarSize == 0) {
            mStatusBarSize = UIUtil.getStatusBarSize(child.getContext());
        }
        if (mTotalDistance == 0) {
            mTotalDistance = parent.getHeight() - dependency.getHeight() - mStatusBarSize;
        }
    }
}
