package com.jackiez.movieproject.views.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jackiez.common.utils.KeyConst;
import com.jackiez.movieproject.model.entities.Movie;
import com.jackiez.movieproject.model.entities.MovieDetail;
import com.jackiez.movieproject.utils.AppDebugLog;
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
                    String shareKey = getIntent().getStringExtra(KeyConst.KEY);
                    getViewDelegate().startAnimation(shareKey);
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
