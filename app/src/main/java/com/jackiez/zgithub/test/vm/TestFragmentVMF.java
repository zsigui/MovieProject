package com.jackiez.zgithub.test.vm;

import com.jackiez.base.rxbus2.RxBus;
import com.jackiez.base.rxbus2.Subscribe;
import com.jackiez.base.util.AppDebugLog;
import com.jackiez.zgithub.test.Mapper;
import com.jackiez.zgithub.test.api.NetDataProvider;
import com.jackiez.zgithub.test.data.ErrorOp;
import com.jackiez.zgithub.test.data.Repo;
import com.jackiez.zgithub.test.view.TestFragment;
import com.jackiez.zgithub.vm.fragment.FDefaultBaseVM;

import java.util.List;

/**
 * Created by zsigui on 17-3-28.
 */

public class TestFragmentVMF extends FDefaultBaseVM<TestFragment> {

    public TestFragmentVMF(TestFragment fragment) {
        super(fragment);
    }

    @Override
    public void registerListener() {
        super.registerListener();
        RxBus.get().register(this);
    }

    @Override
    public void unregisterListener() {
        super.unregisterListener();
        RxBus.get().unregister(this);
    }

    @Override
    public void lazyLoad() {
        // 此处开始调用获取网络数据
        NetDataProvider.obtainData(getActivity(), "c_daily");
    }

    @Override
    public void loadMore() {

    }


    /**
     * 通过 BUS 将数据通知过来
     */
    @Subscribe
    public void handleData(List<Repo> data) {
        AppDebugLog.d(AppDebugLog.TAG_UTIL, "显示处理的数据! " + (data == null));
        List<TestItemRepoVM> repos = Mapper.mapperReposToItemRepoVM(data);
        // 有必要可以再开个线程执行去重，添加等等操作
        AppDebugLog.d(AppDebugLog.TAG_UTIL, "转换后的数据 " + (repos == null));
        getFragment().updateView(repos);
    }

    @Subscribe
    public void handleError(ErrorOp err) {
        AppDebugLog.d(AppDebugLog.TAG_UTIL, "显示错误内容: " + (err != null ? err.t.toString() : "null"));
        if (err != null && err.cmd == NetDataProvider.CMD.GET_REPO) {
            getFragment().showError();
        }
    }
}
