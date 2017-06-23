package com.jackiez.zgithub.test.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jackiez.base.assist.multi_type.MultiTypeAdapter;
import com.jackiez.base.util.AppDebugLog;
import com.jackiez.base.widget.WithFooterRecyclerView;
import com.jackiez.zgithub.R;
import com.jackiez.zgithub.databinding.TestFragmentContentBinding;
import com.jackiez.zgithub.test.adapter.MultiRecylerAdapter;
import com.jackiez.zgithub.test.adapter.TestRepoProvider;
import com.jackiez.zgithub.test.view.layoutmanager.DefaultLinearLayoutManager;
import com.jackiez.zgithub.test.vm.TestFragmentVMF;
import com.jackiez.zgithub.test.vm.TestItemRepoVM;
import com.jackiez.zgithub.view.fragment.DefaultBaseFragment;

import java.util.List;

/**
 * Created by zsigui on 17-3-28.
 */

public class TestFragment extends DefaultBaseFragment<TestFragmentVMF> implements WithFooterRecyclerView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private MultiTypeAdapter mAdapter;
    public List<TestItemRepoVM> mData;
    private TestFragmentContentBinding mContentBinding;

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.test_fragment_content;
    }

    @Override
    protected TestFragmentVMF createVM() {
        return new TestFragmentVMF(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getVModel().lazyLoad();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    public void updateView(List<TestItemRepoVM> data) {
        mData = data;
        if (mAdapter == null) {
            long lastTime = System.currentTimeMillis();
            initContentView();
            AppDebugLog.d(AppDebugLog.TAG_UTIL, "updateView : " + (System.currentTimeMillis() - lastTime) + " ms");
        }
        mAdapter.setItems(mData);
        mAdapter.notifyDataSetChanged();
        AppDebugLog.d(AppDebugLog.TAG_UTIL, "设置数据并通知更新！" + mAdapter.getItemCount());
    }

    private void initContentView() {
        showContent();
        if (getBinding().vsContent.getBinding() == null) {
            AppDebugLog.d(AppDebugLog.TAG_UTIL, "显示不了Content的内容啊!");
            return;
        }
        if (mAdapter != null)
            return;
        AppDebugLog.d(AppDebugLog.TAG_UTIL, "执行inflateContentView");
        mContentBinding = (TestFragmentContentBinding) getBinding().vsContent.getBinding();
        mAdapter = new MultiRecylerAdapter();
        mAdapter.register(TestItemRepoVM.class, new TestRepoProvider());
        LinearLayoutManager llm = new DefaultLinearLayoutManager(getContext());
        mContentBinding.rvContent.setLayoutManager(llm);
        mContentBinding.rvContent.setAdapter(mAdapter);
        mContentBinding.rvContent.setCanShowFooter(true);
        mContentBinding.rvContent.setOnLoadListener(this);
        // 缓存优化设置，设置之后提升效果明显，默认缓存数量只有2（即上下，所以容易出现卡顿）
        mContentBinding.rvContent.setItemViewCacheSize(10);
        mContentBinding.rvContent.setOnClickListener((view) -> {

        });

        mContentBinding.rvContent.setDrawingCacheEnabled(true);
        mContentBinding.rvContent.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mContentBinding.srflContainer.setOnRefreshListener(this);

    }

    @Override
    protected void inflateCallback(int index) {
        super.inflateCallback(index);
        if (index == INDEX_ID_CONTENT) {
            initContentView();
        }
    }

    @Override
    public void onLoadMore() {
        mContentBinding.rvContent.setCanShowFooter(false);
        if (mContentBinding != null) {
            mContentBinding.rvContent.setLoadState(WithFooterRecyclerView.STATE_NONE);
        }
    }

    @Override
    public void onRefresh() {
        AppDebugLog.d(AppDebugLog.TAG_UTIL, "onRefresh is called!");
        if (mContentBinding != null) {
            mContentBinding.srflContainer.setRefreshing(false);
        }
    }

}
