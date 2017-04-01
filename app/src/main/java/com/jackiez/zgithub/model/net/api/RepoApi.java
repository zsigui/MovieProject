package com.jackiez.zgithub.model.net.api;

import com.jackiez.zgithub.model.net.entity.RespRepository;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.FixedQuery;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by zsigui on 17-3-22.
 */
@FixedQuery(key = "per_page", value = "10")
public interface RepoApi {

    @GET("users/repos")
    Observable<List<RespRepository>> getOwnRepos(@Header("Authorization") String auth,
                                                 @Query("page") int page);

    @GET("users/{username}/repos")
    Observable<List<RespRepository>> getSomeoneRepos(@Path("username") String username,
                                                     @Query("page") int page);

    @GET("orgs/{organization}/repos")
    Observable<List<RespRepository>> getOrganizationRepos(@Path("organization") String org,
                                                          @Query("page") int page);

    // 测试发现该接口无法通过 page 和 per_page 属性进行分页，暂未知原因
    @GET("repositories")
    Observable<List<RespRepository>> getPublicRepo();
}
