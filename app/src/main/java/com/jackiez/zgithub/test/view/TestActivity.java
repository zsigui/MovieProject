package com.jackiez.zgithub.test.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jackiez.base.view.bingding.BaseBindingActivity;
import com.jackiez.zgithub.R;
import com.jackiez.zgithub.databinding.TestActivityBinding;
import com.jackiez.zgithub.test.vm.TestActivityVM;

import java.util.ArrayList;


/**
 * Created by zsigui on 17-3-16.
 */

public class TestActivity extends BaseBindingActivity<TestActivityBinding, TestActivityVM> {


//    TestFragment mFragment;

    int[] colors = new int[]{Color.BLUE, Color.CYAN, Color.YELLOW, Color.GREEN};

    private ArrayList<String> data;
    public static class Holder {
        TextView tv;
    }

    @Override
    protected void afterSetContentView(@Nullable Bundle saveInstanceState) {
        super.afterSetContentView(saveInstanceState);
//        if (saveInstanceState == null) {
//            mFragment = TestFragment.newInstance();
//            loadRootFragment(R.id.fl_list, mFragment);
//        } else {
//            mFragment = findFragment(TestFragment.class);
//            showHideFragment(mFragment);
//        }
        initData();
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return data == null ? 0 : data.size();
            }

            @Override
            public String getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Holder holder;
                if (convertView == null) {
                    convertView =  LayoutInflater.from(getApplicationContext()).inflate(R.layout.stack_view, parent, false);
                    holder = new Holder();
                    holder.tv = (TextView) convertView.findViewById(R.id.tv_content);
                    convertView.setTag(holder);
                } else {
                    holder = (Holder) convertView.getTag();
                }
                holder.tv.setText(getItem(position));
                convertView.setBackgroundColor(colors[position]);
                return convertView;
            }
        };
        getBinding().stack.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            data.add("test " + i);
        }
    }

    @Override
    protected TestActivityVM createVM() {
        TestActivityVM vm = new TestActivityVM(this);
        getBinding().setObj(vm);
        return vm;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.test_activity;
    }

}
