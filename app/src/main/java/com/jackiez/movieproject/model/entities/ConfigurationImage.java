package com.jackiez.movieproject.model.entities;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/29
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class ConfigurationImage implements Serializable {

    public String base_url;
    public String secure_base_url;
    public String[] backdrop_sizes;
    public String[] logo_sizes;
    public String[] poster_sizes;
    public String[] profile_sizes;
    public String[] still_sizes;

}
