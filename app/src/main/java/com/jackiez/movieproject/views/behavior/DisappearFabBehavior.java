package com.jackiez.movieproject.views.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jackiez.movieproject.utils.UIUtil;
import com.jackiez.movieproject.views.widget.LobsterTextView;

/**
 * Created by zsigui on 16-10-17.
 */

public class DisappearFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {


    private int mStatusBar;
    private int mFirstTop;
    private int mTotalDistance;

    public DisappearFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {

        Log.d("tag", "child = " + child  + ", dependency = " + dependency);
        return dependency instanceof LobsterTextView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {

        initConf(child, dependency);


        float fade = (mFirstTop - child.getY()) / mTotalDistance;
        Log.d("tag", "fad = " + fade + ", statusBar = " + mStatusBar + ", firstTop = " + mFirstTop + ", c.getPivotY = " + child.getY()
        + ", c.getY = " + child.getY() + ", d.getBottom = " + dependency.getBottom() + ", d.getHeight = " + dependency.getHeight());
        if (fade > 1) {
            fade = 1;
        }
        child.setScaleX(1-fade);
        child.setScaleY(1-fade);

        return true;
    }

    private void initConf(FloatingActionButton child, View dependency) {
        if (mStatusBar == 0) {
            mStatusBar = UIUtil.getStatusBarSize(child.getContext());
        }
        if (mFirstTop == 0) {
            mFirstTop = (int) dependency.getY();
        }
        if (mTotalDistance == 0) {
            mTotalDistance = mFirstTop - mStatusBar;
        }
    }
}
