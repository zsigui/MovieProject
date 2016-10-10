package com.jackiez.movieproject.views.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jackiez.movieproject.model.entities.MovieDetail;
import com.jackiez.movieproject.vp.presenter.AbsPresenterWithViewManager;
import com.jackiez.movieproject.vp.view.MovieDetailVD;

/**
 * Created by zsigui on 16-10-10.
 */

public class MovieDetailActivity extends AbsPresenterWithViewManager<MovieDetail, MovieDetailVD>{


    @Override
    protected void processLogic(Bundle savedInstanceState) {
        super.processLogic(savedInstanceState);

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
