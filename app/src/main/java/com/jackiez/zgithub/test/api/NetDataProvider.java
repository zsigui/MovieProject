package com.jackiez.zgithub.test.api;

import android.content.Context;

import com.jackiez.base.util.AppDebugLog;
import com.jackiez.zgithub.model.net.rx.BaseObserver;
import com.jackiez.zgithub.model.net.rx.RxDefaultTransformer;
import com.jackiez.zgithub.test.BusSingleton;
import com.jackiez.zgithub.test.RetryWhenFunction;
import com.jackiez.zgithub.test.data.ErrorOp;
import com.jackiez.zgithub.test.data.Repo;

import java.util.List;

/**
 * Created by zsigui on 17-3-29.
 */

public class NetDataProvider {

    public interface CMD {
        int GET_REPO = 11;
    }

    public static void obtainData(Context context, String info) {
        BaseObserver<List<Repo>> observer = new BaseObserver<List<Repo>>() {
            @Override
            public void onNext(List<Repo> value) {
                AppDebugLog.d(AppDebugLog.TAG_UTIL, "onNext 通知获取数据: " + value.size());
//                try {
//                    String data = LoganSquare.serialize(value);
//                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "get data = " + data);
//
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                    AppDebugLog.d(AppDebugLog.TAG_UTIL, "change bad = " + e);
//                }
                BusSingleton.get().post(value);
            }

            @Override
            public void onError(Throwable e) {
                AppDebugLog.d(AppDebugLog.TAG_UTIL, "onNext 通知获取失败");
                BusSingleton.get().post(new ErrorOp(CMD.GET_REPO, e));
            }
        };
        NetDataSource.getTestApi().requestRepo(info)
                .retryWhen(new RetryWhenFunction())
                .compose(new RxDefaultTransformer<>(context, false, observer))
                .subscribe(observer);
    }
}
