package com.jackiez.movieproject.vp.view;

import android.animation.Animator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.graphics.Palette;
import android.transition.Transition;
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
import com.jackiez.movieproject.views.listener.defaulimpl.DefaultAnimatorListener;
import com.jackiez.movieproject.views.listener.defaulimpl.DefaultTransitionListener;
import com.jackiez.movieproject.vp.view.base.AbsViewDelegateWithViewManager;

import java.util.List;

/**
 * Created by zsigui on 16-10-10.
 */

public class MovieDetailVD extends AbsViewDelegateWithViewManager<ActivityMovieDetailBinding> implements Palette
        .PaletteAsyncListener {

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
            Palette.from(UIUtil.drawableToBitmap(GlobalCache.sPhotoCache.get())).generate(this);
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startAnimation(String shareKey) {
        mBinding.ivMovieCover.setTransitionName(shareKey);
        mBinding.ltvName.setAlpha(0);
        mBinding.fabStar.setScaleY(0);
        mBinding.fabStar.setScaleX(0);
        mBinding.fabStar.setTag(0);
        mBinding.nsvScroll.setAlpha(0);

        getActivity().getWindow().getSharedElementEnterTransition().addListener(new DefaultTransitionListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTransitionEnd(Transition transition) {
                getActivity().getWindow().getSharedElementEnterTransition().removeListener(this);
                UIUtil.animateRevealShow(mBinding.ltvName, new DefaultAnimatorListener(){
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        UIUtil.animateRevealShow(mBinding.nsvScroll, null);
                        UIUtil.animateScale(mBinding.fabStar, new DefaultAnimatorListener(){

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mBinding.fabStar.setTag(null);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onGenerated(Palette palette) {
        int defaultBgColor = Color.BLACK;
        int defaultFgColor = Color.WHITE;
        Palette.Swatch lightMuted = palette.getLightMutedSwatch();
        Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
        Palette.Swatch darkMuted = palette.getDarkMutedSwatch();
        Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
        int fgColor = (lightVibrant == null ? (lightMuted == null ? defaultFgColor : lightMuted.getRgb())
                : lightVibrant.getRgb());
        int bgColor = (darkMuted == null ? (darkVibrant == null ? defaultBgColor : darkVibrant.getRgb())
                : darkMuted.getRgb());
        mBinding.ltvName.setBackgroundColor(bgColor);
        mBinding.ltvName.setTextColor(fgColor);
        mBinding.toolLayout.setContentScrimColor(bgColor);
        if (darkVibrant != null) {
            mBinding.fabStar.setBackgroundTintList(ColorStateList.valueOf(darkVibrant.getRgb()));
            mBinding.nsvScroll.setBackgroundColor(darkVibrant.getRgb());
        }
        if (lightVibrant != null) {
            int textColor = lightVibrant.getRgb();
            mBinding.tvHomepage.setTextColor(textColor);
            mBinding.tvCompany.setTextColor(textColor);
            mBinding.tvHeaderTagline.setTextColor(bgColor);
            mBinding.tvTagline.setTextColor(textColor);
            mBinding.tvHeaderDescription.setTextColor(bgColor);
            mBinding.tvContent.setTextColor(textColor);
            mBinding.tvHeaderReviews.setTextColor(bgColor);
        }
    }
}
