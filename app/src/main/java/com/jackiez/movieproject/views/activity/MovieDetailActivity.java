package com.jackiez.movieproject.views.activity;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.Transition;

import com.jackiez.common.utils.KeyConst;
import com.jackiez.movieproject.model.entities.Movie;
import com.jackiez.movieproject.model.entities.MovieDetail;
import com.jackiez.movieproject.utils.AppDebugLog;
import com.jackiez.movieproject.utils.UIUtil;
import com.jackiez.movieproject.views.listener.defaulimpl.DefaultAnimatorListener;
import com.jackiez.movieproject.views.listener.defaulimpl.DefaultTransitionListener;
import com.jackiez.movieproject.vp.presenter.AbsPresenterWithViewManager;
import com.jackiez.movieproject.vp.view.MovieDetailVD;

/**
 * Created by zsigui on 16-10-10.
 */

public class MovieDetailActivity extends AbsPresenterWithViewManager<MovieDetail, MovieDetailVD>{


    @Override
    protected void processLogic(Bundle savedInstanceState) {
        super.processLogic(savedInstanceState);
        if (getIntent() != null) {
            try {
                Movie m = (Movie) getIntent().getSerializableExtra(KeyConst.DATA);
                mData = MovieDetail.from(m);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    postponeEnterTransition();
                    setSupportActionBar(getViewDelegate().getBinding().toolbar);
                    setTitle("");
//                    UIUtil.fitViewMargin(getViewDelegate().getBinding().toolbar);
                    String shareKey = getIntent().getStringExtra(KeyConst.KEY);
                    getViewDelegate().getBinding().ivMovieCover.setTransitionName(shareKey);
                    getViewDelegate().getBinding().ltvName.setScaleY(0);
                    getViewDelegate().getBinding().llContent.setScaleY(0);
                    AppDebugLog.d(AppDebugLog.TAG_DEBUG_INFO, "data = " + mData + ", shareKey = " + shareKey);
                    getWindow().getSharedElementEnterTransition().addListener(new DefaultTransitionListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onTransitionEnd(Transition transition) {
                            getWindow().getSharedElementEnterTransition().removeListener(this);
                            UIUtil.animateScale(getViewDelegate().getBinding().ltvName, new DefaultAnimatorListener(){
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    UIUtil.animateScale(getViewDelegate().getBinding().llContent, null);
                                }
                            });
                        }
                    });
                    startPostponedEnterTransition();
                } else {

                }
            } catch (Throwable e) {
                AppDebugLog.e(AppDebugLog.TAG_DEBUG_INFO, e);
            }
        }
    }

    @Override
    protected void loadRefreshDataAsync() {

    }

    @Override
    protected void bindData(@Nullable MovieDetail movieDetail) {
        super.bindData(movieDetail);
        getViewDelegate().showContent();
        getViewDelegate().bindData(movieDetail);
    }

    @Override
    protected Class<MovieDetailVD> getDelegateClass() {
        return MovieDetailVD.class;
    }
}
