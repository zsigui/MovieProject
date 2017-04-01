package com.jackiez.zgithub.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jackiez.base.for_adapter.binding.BaseBindingHolder;
import com.jackiez.zgithub.R;
import com.jackiez.zgithub.databinding.ItemSpinnerDropdownBinding;
import com.jackiez.zgithub.databinding.ItemSpinnerSinceBinding;

import java.util.Locale;

/**
 * Created by zsigui on 17-3-20.
 */

public class ToolbarSpinnerAdapter extends BaseAdapter {

    private static final int TAG_OBJECT = 0x12333333;
    private final String[] data = new String[]{"Today", "This Week", "This Month"};

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public String getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToolbarSpinnerHolder holder;
        if (convertView == null) {
            ItemSpinnerSinceBinding b = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_spinner_since, parent, false);
            holder = new ToolbarSpinnerHolder(b.getRoot(), b);
            convertView = b.getRoot();
            convertView.setTag(TAG_OBJECT, holder);
        } else {
            holder = (ToolbarSpinnerHolder) convertView.getTag(TAG_OBJECT);
        }
        holder.bindData(getItem(position), position);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ToolbarSpinnerDropDownHolder holder;
        if (convertView == null) {
            ItemSpinnerDropdownBinding b = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_spinner_dropdown, parent, false);
            holder = new ToolbarSpinnerDropDownHolder(b.getRoot(), b);
            convertView = b.getRoot();
            convertView.setTag(TAG_OBJECT, holder);
        } else {
            holder = (ToolbarSpinnerDropDownHolder) convertView.getTag(TAG_OBJECT);
        }
        holder.bindData(getItem(position), position);
        return convertView;
    }

    private static class ToolbarSpinnerHolder extends BaseBindingHolder<String, ItemSpinnerSinceBinding> {

        public ToolbarSpinnerHolder(@NonNull View itemView, @NonNull ItemSpinnerSinceBinding binding) {
            super(itemView, binding);
        }

        @Override
        public void bindData(String s, int position) {
            getBinding().tvText.setText(String.format(Locale.ENGLISH, "About %s", s));
        }
    }

    private static class ToolbarSpinnerDropDownHolder extends BaseBindingHolder<String, ItemSpinnerDropdownBinding> {

        public ToolbarSpinnerDropDownHolder(@NonNull View itemView, @NonNull ItemSpinnerDropdownBinding binding) {
            super(itemView, binding);
        }

        @Override
        public void bindData(String s, int position) {
            getBinding().tvText.setText(s);
        }
    }
}
