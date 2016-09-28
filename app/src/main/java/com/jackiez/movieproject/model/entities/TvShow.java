package com.jackiez.movieproject.model.entities;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/29
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class TvShow {
    private String backdrop_path;
    private String first_air_date;
    private int id;
    private String name;
    private List origin_country;
    private String original_name;
    private float popularity;
    private String poster_path;
    private float vote_average;
    private int vote_count;
}
