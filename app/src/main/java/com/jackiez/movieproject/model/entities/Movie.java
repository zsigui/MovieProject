package com.jackiez.movieproject.model.entities;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bluelinelabs.logansquare.annotation.JsonIgnore;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jackiez.movieproject.BR;

import java.io.Serializable;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class Movie extends BaseObservable implements Serializable {

    private String adult;
    private String backdrop_path;
    private String id;
    private String original_title;
    private String release_date;
    private String poster_path;
    private String popularity;
    private String title;
    private String vote_average;
    private String vote_count;
    private String overview;
    @JsonIgnore
    private boolean movieReady;


    public Movie() {
    }

    public Movie(Movie m) {
        if (m == null)
            return;
        setPoster_path(m.getPoster_path());
        setPopularity(m.getPopularity());
        setOverview(m.getOverview());
        setAdult(m.getAdult());
        setBackdrop_path(m.getBackdrop_path());
        setId(m.getId());
        setAdult(m.getAdult());
        setOriginal_title(m.getOriginal_title());
        setRelease_date(m.getRelease_date());
        setTitle(m.getTitle());
        setVote_average(m.getVote_average());
        setVote_count(m.getVote_count());
    }

    @Bindable
    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
        notifyPropertyChanged(BR.adult);
    }


    @Bindable
    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
        notifyPropertyChanged(BR.backdrop_path);
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
        notifyPropertyChanged(BR.original_title);
    }

    @Bindable
    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
        notifyPropertyChanged(BR.release_date);
    }

    @Bindable
    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
        notifyPropertyChanged(BR.poster_path);
    }

    @Bindable
    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
        notifyPropertyChanged(BR.popularity);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
        notifyPropertyChanged(BR.vote_average);
    }

    @Bindable
    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
        notifyPropertyChanged(BR.vote_count);
    }

    @Bindable
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
        notifyPropertyChanged(BR.overview);
    }

    @Bindable
    public boolean isMovieReady() {
        return movieReady;
    }

    public void setMovieReady(boolean movieReady) {
        this.movieReady = movieReady;
        notifyPropertyChanged(BR.movieReady);
    }
}
