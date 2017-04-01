package com.jackiez.base.for_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zsigui on 17-3-16.
 */
public abstract class BaseHolder<Data> extends RecyclerView.ViewHolder{

    public static final int TAG_POS = 0x12341122;
    public static final int TAG_ITEM = 0x1343FFF1;

    public BaseHolder(View itemView) {
        super(itemView);
    }

    /**
     * 重写该方法进行数据绑定
     */
    public abstract void bindData(@NonNull Data data, int position);

}
