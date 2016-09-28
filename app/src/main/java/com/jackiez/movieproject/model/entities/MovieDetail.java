package com.jackiez.movieproject.model.entities;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;
import java.util.List;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class MovieDetail implements Serializable {

    private String backdrop_path;
    private MovieCollection belongs_to_collection;
    private String homepage;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private String poster_path;
    private String release_date;
    private String status;
    private String tagline;
    private String title;
    private List<ProductionCountry> production_countries;
    private List<Genres> genres;
    private List<SpokenLanguage> spoken_languages;
    private List<ProductionCompany> production_companies;
    private List<MovieImage> movieImagesList;
    private int budget;
    private int id;
    private float popularity;
    private int revenue;
    private int runtime;
    private float vote_average;
    private int vote_count;
    private boolean adult;
    private boolean video;
}
