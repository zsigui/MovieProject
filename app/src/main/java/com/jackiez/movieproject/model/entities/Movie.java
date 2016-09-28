package com.jackiez.movieproject.model.entities;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Movie implements Serializable {

    public String adult;
    public String backdrop_path;
    public String id;
    public String original_title;
    public String release_date;
    public String poster_path;
    public String popularity;
    public String title;
    public String vote_average;
    public String vote_count;
    public String overview;
    public boolean movieReady;
}
