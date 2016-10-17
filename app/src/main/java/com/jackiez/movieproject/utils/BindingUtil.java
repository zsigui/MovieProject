package com.jackiez.movieproject.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.common.base.Strings;
import com.jackiez.common.utils.RestConst;
import com.jackiez.movieproject.R;
import com.jackiez.movieproject.model.entities.ProductionCompany;

import java.util.List;

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
                .placeholder(R.drawable.london_flat)
                .error(R.drawable.awesome)
                .centerCrop()
                .dontAnimate()  // 如果不配置该句，会出现centerCrop等scaleType设置不执行情况
                .into(iv);
//        iv.setImageURI(url);
    }

    @BindingAdapter({"android:text"})
    public static void setCompany(TextView tv, List<ProductionCompany> companies) {
        if (companies == null) return;
        StringBuilder sb = new StringBuilder();
        for (ProductionCompany company : companies) {
            sb.append(company.name).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
    }

    public static <T extends ViewDataBinding> T create(Context context, @LayoutRes int layotuId, ViewGroup parent) {
        return DataBindingUtil.inflate(LayoutInflater.from(context), layotuId, parent, false);
    }
}
