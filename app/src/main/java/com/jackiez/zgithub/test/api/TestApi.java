package com.jackiez.zgithub.test.api;

import com.jackiez.zgithub.test.data.Repo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by zsigui on 17-3-28.
 */

public interface TestApi {

    // info : "lan_timespan"
    @GET("json/{info}")
    Observable<List<Repo>> requestRepo(@Path("info") String info);
}
