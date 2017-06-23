package com.jackiez.zgithub.model.net;

import com.jackiez.base.util.AppDebugLog;
import com.jackiez.base.util.NetworkUtil;
import com.jackiez.base.util.io.FileUtil;
import com.jackiez.zgithub.ZGitHubApp;
import com.jackiez.zgithub.model.DataConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.logansquare.CustomLoganSquareConverterFactory;

/**
 * 用于实例化 Retrofit 类的单例数据源
 * <p>
 * Created by zsigui on 17-3-21.
 */

public class NetDataSource {

    private static final int READ_TIMEOUT = 60;
    private static final int CONNECT_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 60;
    private static final int CACHE_SIZE = 100 * 1024 * 1024;
    private static final String BASE_HOST = "https://api.github.com/";

    private static Retrofit mDefaultRetrofit;

    public static Retrofit getRetrofit() {
        if (mDefaultRetrofit == null) {
            synchronized (NetDataSource.class) {
                if (mDefaultRetrofit == null) {
                    mDefaultRetrofit = new NetDataSource().createRetrofit();
                }
            }
        }
        return mDefaultRetrofit;
    }

    private Retrofit createRetrofit() {

        return new Retrofit.Builder()
                .addConverterFactory(CustomLoganSquareConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.newThread()))
                .baseUrl(BASE_HOST)
                .client(createDefaultClient())
                .build();
    }

    private OkHttpClient createDefaultClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(4, 5, TimeUnit.MINUTES))
                .cache(createDefaultCache())
                .addInterceptor(createDefaultInterceptor())
                .build();
    }

    private Interceptor createDefaultInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest;
                newRequest = chain.request().newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
                AppDebugLog.d(AppDebugLog.TAG_NET, "请求地址：" + newRequest.url().uri());
                Response response = chain.proceed(newRequest);

                CacheControl cacheControl;
                if (NetworkUtil.isConnected(ZGitHubApp.get())) {
                    cacheControl = new CacheControl.Builder()
                            .noCache()
                            .build();
                } else {
                    cacheControl = new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(365, TimeUnit.DAYS)
                            .build();
                }
                String cacheControlStr = cacheControl.toString();

                return response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", cacheControlStr)
                        .build();
            }
        };
    }

    private Cache createDefaultCache() {
        File httpCacheDir = FileUtil.getOwnCacheDirectory(ZGitHubApp.get(), DataConfig.NET_CACHE, true);
        return httpCacheDir == null ? null : new Cache(httpCacheDir, CACHE_SIZE);
    }

}
