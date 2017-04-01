package com.jackiez.base.for_adapter.binding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackiez.base.assist.multi_type.ItemViewProvider;
import com.jackiez.base.util.AppDebugLog;

/**
 * Created by zsigui on 17-3-16.
 */
@SuppressWarnings("unchecked")
public abstract class BaseBindingProvider<Data, B extends ViewDataBinding, VH extends BaseBindingHolder>
        extends ItemViewProvider<Data, VH> {

    /**
     * 重写该方法返回构建项的布局ID
     */
    @LayoutRes
    protected abstract int getLayoutId();

    @NonNull
    @Override
    protected VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        long lastTime = System.currentTimeMillis();
        B binding = createDataBinding(inflater, getLayoutId(), parent);
        VH vh = createHolder(binding.getRoot(), binding);
        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onCreateViewHolder speed time : " + (System.currentTimeMillis() - lastTime) + " ms");
        return vh;
    }

    /**
     * 重写该方法，返回 BaseHolder 类型的对象实例 <br />
     * <br />
     * <usage>
     * Usage: return new VH(itemView, binding);
     * </usage>
     *
     * @return
     */
    protected abstract VH createHolder(View itemView, B binding);

    @Override
    protected void onBindViewHolder(@NonNull VH holder, @NonNull Data data, @NonNull int position) {
        long lastTime = System.currentTimeMillis();
        holder.bindData(data, position);
        long diff = (System.currentTimeMillis() - lastTime);
        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onBindViewHolder speed time : " + diff + " ms");
    }

    /**
     * 根据传入的参数创建返回指定的ViewDataBinding类型的实例
     */
    public static <T extends ViewDataBinding>  T createDataBinding(LayoutInflater inflater,
                                                             @LayoutRes int layoutId,
                                                             ViewGroup parent) {
        ViewDataBinding b = DataBindingUtil.inflate(inflater, layoutId, parent, false);
        return (T) b;
    }
}
