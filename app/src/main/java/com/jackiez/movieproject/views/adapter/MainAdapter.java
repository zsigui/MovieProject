package com.jackiez.movieproject.views.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.jackiez.movieproject.R;
import com.jackiez.movieproject.databinding.ItemHomeMovieBinding;
import com.jackiez.movieproject.model.entities.Movie;
import com.jackiez.movieproject.utils.BindingUtil;
import com.jackiez.movieproject.views.adapter.base.BaseHolder;
import com.jackiez.movieproject.views.adapter.base.BaseRVAdapter;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/6
 */

public class MainAdapter extends BaseRVAdapter<Movie>{
    public MainAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeMovieHolder((ItemHomeMovieBinding) BindingUtil.create(mContext,
                R.layout.item_home_movie, parent));
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        ((HomeMovieHolder) holder).bindData(getItem(position), position, this);
    }

    public static class HomeMovieHolder extends BaseHolder<ItemHomeMovieBinding> {


        public HomeMovieHolder(ItemHomeMovieBinding binding) {
            super(binding);
        }

        public void bindData(final Movie movie, int position, View.OnClickListener listener) {

            mBinding.setMovie(movie);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                mBinding.ivMovieCover.setTransitionName("cover" + position);
            mBinding.ivMovieCover.setTag(TAG_POSITION, position);
            mBinding.ivMovieCover.setOnClickListener(listener);
//            mBinding.tvMovieTitle.setText(movie.getTitle());
//            String posterURL = RestConst.BASE_MOVIE_HOST + movie.getPoster_path();
//            Glide.with(itemView.getContext())
//                    .load(posterURL)
//                    .centerCrop()
//                    .into(mBinding.ivMovieCover);
            mBinding.executePendingBindings();
        }
    }
}
