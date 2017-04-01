package com.jackiez.zgithub.test.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jackiez.zgithub.R;
import com.jackiez.zgithub.databinding.TestItemRepoBinding;
import com.jackiez.zgithub.test.vm.TestItemRepoVM;

import java.util.List;

/**
 * Created by zsigui on 17-3-30.
 */

public class TestRepoAdapter extends BaseAdapter {

    final static int TAG_OBJ = 0x1244FFEE;

    List<TestItemRepoVM> mData;
    Context mContext;
    LayoutInflater mInflater;

    public TestRepoAdapter(Context context) {
        this(null, context);
    }

    public TestRepoAdapter(List<TestItemRepoVM> data, Context context) {
        mData = data;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<TestItemRepoVM> getData() {
        return mData;
    }

    public void setData(List<TestItemRepoVM> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public TestItemRepoVM getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TestRepoProvider.TestRepoVH vh;
        if (convertView == null) {
            TestItemRepoBinding binding = DataBindingUtil.inflate(mInflater, R.layout.test_item_repo, parent, false);
            vh = new TestRepoProvider.TestRepoVH(binding.getRoot(), binding);
            convertView = binding.getRoot();
            convertView.setTag(TAG_OBJ, vh);
        } else {
            vh = (TestRepoProvider.TestRepoVH) convertView.getTag(TAG_OBJ);
        }
        vh.bindData(getItem(position), position);
        return convertView;
    }
}
