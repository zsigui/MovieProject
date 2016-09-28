package com.jackiez.common.utils;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

public class RestConst {

    // https://api.themoviedb.org/3/movie/popular?api_key=b947b6d92d1e94ee7c21f3597ca39dbf

    public static final String QUERY_API_KEY = "api_key";

    public static final String PATH_ID = "id";

    public static final String BASE_MOVIE_HOST = "https://api.themoviedb.org/3/";

    public static final String GET_POPULAR_MOVIES = "/movie/popular";

    public static final String GET_MOVIE_DETAIL = "/movie/{id}";

    public static final String GET_POPULAR_MOVIES_BY_PAGE = "/movie/popular";

    public static final String GET_MOVIE_REVIEWS = "/movie/{id}/reviews";

    public static final String GET_MOVIE_IMAGES = "/movie/{id}/images";

    public static final String GET_CONFIGURATION = "/configuration";

}
