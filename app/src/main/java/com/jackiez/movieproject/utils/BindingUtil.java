package com.jackiez.movieproject.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.common.base.Strings;
import com.jackiez.common.utils.RestConst;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/6
 */

public class BindingUtil {


    @BindingAdapter({"image"})
    public static void loadImage(ImageView iv, String url) {
        if (iv == null) return;
        if (Strings.isNullOrEmpty(url)) return;
        url = RestConst.BASE_PICS_SRC + url;
        Glide.with(iv.getContext())
                .load(url)
//                .placeholder(R.drawable.awesome)
                .error(android.R.drawable.ic_delete)
                .centerCrop()
                .crossFade()
                .into(iv);
//        iv.setImageURI(url);
    }

    public static <T extends ViewDataBinding> T create(Context context, @LayoutRes int layotuId, ViewGroup parent) {
        return DataBindingUtil.inflate(LayoutInflater.from(context), layotuId, parent, false);
    }
}
