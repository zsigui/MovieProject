package com.jackiez.movieproject.vp.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jackiez.movieproject.R;
import com.jackiez.movieproject.common.GlobalCache;
import com.jackiez.movieproject.databinding.ActivityMovieDetailBinding;
import com.jackiez.movieproject.model.entities.MovieDetail;
import com.jackiez.movieproject.model.entities.MovieReview;
import com.jackiez.movieproject.utils.BindingUtil;
import com.jackiez.movieproject.utils.UIUtil;
import com.jackiez.movieproject.views.activity.MovieDetailActivity;
import com.jackiez.movieproject.vp.view.base.AbsViewDelegateWithViewManager;

import java.util.List;

/**
 * Created by zsigui on 16-10-10.
 */

public class MovieDetailVD extends AbsViewDelegateWithViewManager<ActivityMovieDetailBinding> {

    public MovieDetailVD(Context context) {
        super(context);
        mViewManager.setContainer(getBinding().rlContainer);
        mViewManager.setContentView(getBinding().llContent);
    }

    @Override
    public View getRootView() {
        return mBinding.getRoot();
    }

    @Override
    public void processLogic(Bundle saveInstanceState) {
        MovieDetailActivity activity = getActivity();
        UIUtil.setTranslucentStatusBar(activity);
//        UIUtil.fitViewMargin(mBinding.ltvName);

    }

    @Override
    public void setListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_movie_detail;
    }

    public void bindData(MovieDetail movie) {
        mBinding.setMovie(movie);
        BindingUtil.setCompany(mBinding.tvCompany, movie.getProduction_companies());
        if (GlobalCache.sPhotoCache != null) {
            mBinding.ivMovieCover.setImageDrawable(GlobalCache.sPhotoCache.get());
        } else {
            BindingUtil.loadImage(mBinding.ivMovieCover, movie.getPoster_path());
        }
    }

    public void bindReviews(List<MovieReview> reviews) {
        if (reviews != null) {
            mBinding.tvHeaderReviews.setVisibility(View.VISIBLE);
            if (reviews.isEmpty()) {
                TextView emptyTV = new TextView(getRootView().getContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    emptyTV.setTextAppearance(R.style.MaterialMoviesHeaderTextView);
                } else {
                    emptyTV.setTextAppearance(getRootView().getContext(), R.style.MaterialMoviesHeaderTextView);
                }


            }
        }
    }
}
