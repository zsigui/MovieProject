package com.jackiez.movieproject.model.entities;

import android.databinding.Bindable;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.jackiez.movieproject.BR;

import java.util.List;

/**
 * @author JackieZhuang
 * @email zsigui@foxmail.com
 * @date 2016/9/28
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class MovieDetail extends Movie {

    private MovieCollection belongs_to_collection;
    private String homepage;
    private String imdb_id;
    private String original_language;
    private String status;
    private String tagline;
    private List<ProductionCountry> production_countries;
    private List<Genres> genres;
    private List<SpokenLanguage> spoken_languages;
    private List<ProductionCompany> production_companies;
    private List<MovieImage> movieImagesList;
    private int budget;
    private int revenue;
    private int runtime;
    private boolean video;

    public MovieDetail() {
    }

    public MovieDetail(Movie m) {
        super(m);
    }

    @Bindable
    public MovieCollection getBelongs_to_collection() {
        return belongs_to_collection;
    }

    public void setBelongs_to_collection(MovieCollection belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
        notifyPropertyChanged(BR.belongs_to_collection);
    }

    @Bindable
    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
        notifyPropertyChanged(BR.homepage);
    }

    @Bindable
    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
        notifyPropertyChanged(BR.imdb_id);
    }

    @Bindable
    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
        notifyPropertyChanged(BR.original_language);
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
        notifyPropertyChanged(BR.tagline);
    }

    @Bindable
    public List<ProductionCountry> getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(List<ProductionCountry> production_countries) {
        this.production_countries = production_countries;
        notifyPropertyChanged(BR.production_countries);
    }

    @Bindable
    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
        notifyPropertyChanged(BR.genres);
    }

    @Bindable
    public List<SpokenLanguage> getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(List<SpokenLanguage> spoken_languages) {
        this.spoken_languages = spoken_languages;
        notifyPropertyChanged(BR.spoken_languages);
    }

    @Bindable
    public List<ProductionCompany> getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(List<ProductionCompany> production_companies) {
        this.production_companies = production_companies;
        notifyPropertyChanged(BR.production_companies);
    }

    @Bindable
    public List<MovieImage> getMovieImagesList() {
        return movieImagesList;
    }

    public void setMovieImagesList(List<MovieImage> movieImagesList) {
        this.movieImagesList = movieImagesList;
        notifyPropertyChanged(BR.movieImagesList);
    }

    @Bindable
    public int getBudget() {
        return budget;
    }

    @Bindable
    public void setBudget(int budget) {
        this.budget = budget;
        notifyPropertyChanged(BR.budget);
    }

    @Bindable
    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
        notifyPropertyChanged(BR.revenue);
    }

    @Bindable
    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
        notifyPropertyChanged(BR.runtime);
    }

    @Bindable
    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
        notifyPropertyChanged(BR.video);
    }

    public static MovieDetail from(Movie m) {
        if (m == null)
            return null;
        return new MovieDetail(m);
    }
}
