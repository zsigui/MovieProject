package com.jackiez.zgithub.model.net;

import com.jackiez.zgithub.model.net.api.RepoApi;
import com.jackiez.zgithub.model.net.api.SearchApi;
import com.jackiez.zgithub.model.net.api.UserApi;

/**
 * Created by zsigui on 17-3-21.
 */

public class NetRestProvider {

    private static UserApi sUserApi;
    private static RepoApi sRepoApi;
    private static SearchApi sSearchApi;


    /**
     * 获取请求获取用户信息的接口服务实例对象
     * @return
     */
    public static UserApi getUserApi() {
        if (sUserApi == null) {
            sUserApi = NetDataSource.getRetrofit().create(UserApi.class);
        }
        return sUserApi;
    }

    public static SearchApi getSearchApi() {
        if (sSearchApi == null) {
            sSearchApi = NetDataSource.getRetrofit().create(SearchApi.class);
        }
        return sSearchApi;
    }

    public static RepoApi getRepoApi() {
        if (sRepoApi == null) {
            sRepoApi = NetDataSource.getRetrofit().create(RepoApi.class);
        }
        return sRepoApi;
    }
}
