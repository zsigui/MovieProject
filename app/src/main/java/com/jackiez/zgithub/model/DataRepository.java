package com.jackiez.zgithub.model;

import android.content.Context;
import android.text.TextUtils;

import com.jackiez.zgithub.model.net.NetRestProvider;
import com.jackiez.zgithub.model.net.entity.RespUser;
import com.jackiez.zgithub.model.net.rx.BaseObserver;
import com.jackiez.zgithub.model.net.rx.RxDefaultTransformer;

import java.util.Locale;

/**
 * Created by zsigui on 17-3-22.
 */

public class DataRepository {

    /**
     * 执行登录验证操作，事件的回调通过 EventBus 发送事件来处理
     * @param username
     * @param password
     */
    public void login(Context c, String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
            return;
        String auth = String.format(Locale.CHINA, "%s:%s", username, password);
        BaseObserver<RespUser> observer = new BaseObserver<RespUser>() {
            @Override
            public void onNext(RespUser value) {
            }
        };
        NetRestProvider.getUserApi().loginAuth(auth)
                .compose(new RxDefaultTransformer<>(c, true, observer))
                .subscribe(observer);
    }

    public void getRepo(String username, int page) {
        if (TextUtils.isEmpty(username))
            return;
        NetRestProvider.getRepoApi().getSomeoneRepos(username, page);
    }
}
