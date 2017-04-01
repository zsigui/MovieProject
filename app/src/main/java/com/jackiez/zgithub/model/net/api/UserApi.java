package com.jackiez.zgithub.model.net.api;

import com.jackiez.zgithub.model.net.entity.RespUser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.FixedQuery;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zsigui on 17-3-21.
 */
public interface UserApi {

    /**
     *
     * @param  auth a String like "{username}:{password}"
     * @return
     */
    @GET("users")
    Observable<RespUser> loginAuth(@Header("Authorization") String auth);

    @GET("users/{username}")
    Observable<RespUser> getUser(@Path("username") String username);

    @GET("users/{username}/followers")
    @FixedQuery(key = "per_page", value = "20")
    Observable<List<RespUser>> getFollowers(@Path("username") String username,
                                            @Query("page") int page);

    @GET("users/{username}/following")
    @FixedQuery(key = "per_page", value = "20")
    Observable<List<RespUser>> getFollowing(@Path("username") String username,
                                            @Query("page") int page);

}
