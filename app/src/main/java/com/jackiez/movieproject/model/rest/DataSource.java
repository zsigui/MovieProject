package com.jackiez.movieproject.model.rest;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;
import com.jackiez.common.utils.Constants;
import com.jackiez.common.utils.RestConst;
import com.jackiez.movieproject.MovieApp;
import com.jackiez.movieproject.utils.AppDebugLog;
import com.jackiez.movieproject.utils.FileUtil;
import com.jackiez.movieproject.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by zsigui on 16-9-29.
 */

public class DataSource {

    private static final class SingletonHolder {
        static DataSource sInstance = new DataSource();
    }

    public static DataSource getInstance() {
        return SingletonHolder.sInstance;
    }

    private DataSource() {}

    private MovieEngineAPI mMovieEngineAPI;

    public MovieEngineAPI getMovieEngineApi() {
        if (mMovieEngineAPI == null) {
            synchronized (Object.class) {
                if (mMovieEngineAPI == null) {
                    mMovieEngineAPI = createMovieEngineApi();
                }
            }
        }
        return mMovieEngineAPI;
    }

    private MovieEngineAPI createMovieEngineApi() {
        return createRetrofit().create(MovieEngineAPI.class);
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(LoganSquareConverterFactory.create())
                .client(createDefaultClient())
                .baseUrl(RestConst.BASE_MOVIE_HOST)
                .build();
    }

    private OkHttpClient createDefaultClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.MICROSECONDS)
                .readTimeout(Constants.READ_TIMEOUT, TimeUnit.MICROSECONDS)
                .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.MICROSECONDS)
                .retryOnConnectionFailure(false)
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
                if (NetworkUtil.isConnected(MovieApp.getInstance())) {
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
        File httpCacheDir = FileUtil.getOwnCacheDirectory(MovieApp.getInstance(), Constants.NET_CACHE_NAME, true);
        return httpCacheDir == null ? null : new Cache(httpCacheDir, 100 * 1024 * 1024);
    }
}
