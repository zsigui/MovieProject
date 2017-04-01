package com.jackiez.zgithub.common;

/**
 * Created by zsigui on 17-3-21.
 */

public final class RestConfig {

    private RestConfig(){}

    public static final String BASE_HOST = "https://api.github.com/";

    public static final String SEARCH_REPOSITORIES = "search/repositories";

    public static final String USER = "/users/{username}";
}
