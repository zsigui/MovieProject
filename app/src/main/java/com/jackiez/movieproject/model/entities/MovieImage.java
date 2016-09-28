package com.jackiez.movieproject.model.entities;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class MovieImage implements Serializable {
    public int width;
    public int height;
    public String file_path;
    public String iso_639_1;
    public float aspect_ratio;
    public float vote_average;
    public int vote_count;
}
