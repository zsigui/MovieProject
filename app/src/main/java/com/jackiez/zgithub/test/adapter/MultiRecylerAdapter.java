package com.jackiez.zgithub.test.adapter;

import android.support.v7.widget.RecyclerView;

import com.jackiez.base.assist.multi_type.MultiTypeAdapter;
import com.jackiez.base.for_adapter.binding.BaseBindingHolder;

/**
 * Created by zsigui on 17-3-30.
 */

public class MultiRecylerAdapter extends MultiTypeAdapter {


    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder != null && holder instanceof BaseBindingHolder) {
            ((BaseBindingHolder) holder).onViewRecycled();
        }
    }
}
