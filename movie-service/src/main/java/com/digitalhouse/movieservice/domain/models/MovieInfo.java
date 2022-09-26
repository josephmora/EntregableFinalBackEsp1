package com.digitalhouse.movieservice.domain.models;

import java.io.Serializable;
import java.util.List;

public class MovieInfo implements Serializable {

    private static final long serialVersionUID = 2511762901120961939L;

    private String createdDate;
    private String genre;
    private List<Movie> movies;

    public MovieInfo() {
        //No-args constructor
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
