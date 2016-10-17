package com.jackiez.movieproject.views.listener;

import android.view.View;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/10/6
 */

public interface OnItemClickListener<Data> {
    void itemClick(View v, int position, Data item);
}
