package com.jackiez.movieproject.model.rest;

import com.jackiez.common.utils.RestConst;
import com.jackiez.movieproject.model.entities.ConfigurationInfo;
import com.jackiez.movieproject.model.entities.Movie;
import com.jackiez.movieproject.model.entities.MovieDetail;
import com.jackiez.movieproject.model.entities.MovieImage;
import com.jackiez.movieproject.model.entities.PageData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

public interface MovieEngineAPI {

    @GET(RestConst.GET_MOVIE_DETAIL)
    Observable<MovieDetail> getMovieDetail (
            @Query(RestConst.QUERY_API_KEY) String apiKey,
            @Path(RestConst.PATH_ID) int id
    );

    @GET(RestConst.GET_POPULAR_MOVIES_BY_PAGE)
    Observable<PageData<Movie>> getPopularMoviesByPage(
            @Query(RestConst.QUERY_API_KEY) String apiKey,
            @Query(RestConst.QUERY_PAGE) int page
    );

    @GET(RestConst.GET_CONFIGURATION)
    Observable<ConfigurationInfo> getConfiguration (
            @Query(RestConst.QUERY_API_KEY) String apiKey
    );

    @GET(RestConst.GET_MOVIE_REVIEWS)
    Observable<PageData<MovieImage>> getMovieReviews (
            @Query(RestConst.QUERY_API_KEY) String apiKey,
            @Path(RestConst.PATH_ID) int id
    );

    @GET(RestConst.GET_MOVIE_IMAGES)
    Observable<PageData<MovieImage>> getMovieImages (
            @Query(RestConst.QUERY_API_KEY) String apiKey,
            @Path(RestConst.PATH_ID) int id
    );
}
