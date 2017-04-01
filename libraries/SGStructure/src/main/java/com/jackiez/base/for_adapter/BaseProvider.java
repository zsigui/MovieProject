package com.jackiez.base.for_adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackiez.base.assist.multi_type.ItemViewProvider;

/**
 * Created by zsigui on 17-3-16.
 */

public abstract class BaseProvider<Data, VH extends BaseHolder<Data>> extends ItemViewProvider<Data, VH> {


    /**
     * 重写该方法返回构建项的布局ID
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 重写该方法，返回 BaseHolder 类型的对象实例 <br />
     * <br />
     * <usage>
     * Usage: return new VH(itemView);
     * </usage>
     *
     * @return
     */
    protected abstract VH createHolder(View itemView);

    @NonNull
    @Override
    protected VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return createHolder(inflater.inflate(getLayoutId(), parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull VH holder, @NonNull Data data, int position) {
        holder.bindData(data, position);
    }
}
