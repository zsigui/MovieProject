package com.jackiez.movieproject.model.rest;

import com.jackiez.common.utils.RestConst;
import com.jackiez.movieproject.model.entities.ConfigurationInfo;
import com.jackiez.movieproject.model.entities.Movie;
import com.jackiez.movieproject.model.entities.MovieDetail;
import com.jackiez.movieproject.model.entities.MovieImage;
import com.jackiez.movieproject.model.entities.PageData;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

public interface MovieEngineAPI {

    @GET(RestConst.GET_POPULAR_MOVIES)
    Callback<PageData<Movie>> getPopularMovies(
            @Query(RestConst.QUERY_API_KEY) String apiKey
    );

    @GET(RestConst.GET_MOVIE_DETAIL)
    Callback<MovieDetail> getMovieDetail (
            @Query(RestConst.QUERY_API_KEY) String apiKey,
            @Path(RestConst.PATH_ID) String id
    );

    @GET(RestConst.GET_POPULAR_MOVIES_BY_PAGE)
    Callback<PageData<Movie>> getPopularMoviesByPage(
            @Query(RestConst.QUERY_API_KEY) String apiKey,
            @Path(RestConst.PATH_ID) String id
    );

    @GET(RestConst.GET_CONFIGURATION)
    Callback<ConfigurationInfo> getConfiguration (
            @Query(RestConst.QUERY_API_KEY) String apiKey
    );

    @GET(RestConst.GET_MOVIE_REVIEWS)
    Callback<PageData<MovieImage>> getMovieReviews (
            @Query(RestConst.QUERY_API_KEY) String apiKey,
            @Path(RestConst.PATH_ID) String id
    );

    @GET(RestConst.GET_MOVIE_IMAGES)
    Callback<PageData<MovieImage>> getMovieImages (
            @Query(RestConst.QUERY_API_KEY) String apiKey,
            @Path(RestConst.PATH_ID) String id
    );
}
