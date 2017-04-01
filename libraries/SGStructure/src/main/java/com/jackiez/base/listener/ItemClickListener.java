package com.jackiez.base.listener;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * 提供给列表项使用同一个接口监听回调处理
 *
 * Created by zsigui on 17-3-17.
 */
public interface ItemClickListener<T> {

    void onItemClick(View v, int pos, @Nullable T data);
}
