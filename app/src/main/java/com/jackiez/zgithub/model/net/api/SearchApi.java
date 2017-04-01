package com.jackiez.zgithub.model.net.api;

import com.jackiez.zgithub.model.net.entity.RespRepository;
import com.jackiez.zgithub.model.net.entity.RespSearchResult;
import com.jackiez.zgithub.model.net.entity.RespUser;

import io.reactivex.Observable;
import retrofit2.http.FixedQuery;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zsigui on 17-3-22.
 */

/**
 * Search 使用说明参考 : https://developer.github.com/v3/search/#search-{users/repositories}
 */
@FixedQuery(key = "per_page", value = "10")
public interface SearchApi {

    @GET("search/users")
    Observable<RespSearchResult<RespUser>> searchUsers(@Query("q") String q,
                                                       @Query("page") String page);

    @GET("search/users")
    Observable<RespSearchResult<RespUser>> searchUsers(@Query("q") String q,
                                                       @Query("sort") String sort,
                                                       @Query("order") String order,
                                                       @Query("page") String page);

    @GET("search/repositories")
    Observable<RespSearchResult<RespRepository>> searchRepo(@Query("q") String q,
                                                            @Query("page") String page);

    @GET("search/repositories")
    Observable<RespSearchResult<RespRepository>> searchRepo(@Query("q") String q,
                                                            @Query("sort") String sort,
                                                            @Query("order") String order,
                                                            @Query("page") String page);

}
