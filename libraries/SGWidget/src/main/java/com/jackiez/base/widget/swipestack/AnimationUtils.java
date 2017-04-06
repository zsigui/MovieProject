package com.jackiez.base.widget.swipestack;

import android.animation.Animator;

/**
 * Created by zsigui on 17-4-5.
 */

public class AnimationUtils {

    public static abstract class AnimationEndListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {
            // Do nothing
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            // Do nothing
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            // Do nothing
        }
    }
}