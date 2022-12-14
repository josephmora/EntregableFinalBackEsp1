package com.digitalhouse.catalogservice.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("MovieQueue")
public class Movie {

    private String id;
    private String name;
    private String genre;
    private String urlStream;

    public Movie() {
        //No-args constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUrlStream() {
        return urlStream;
    }

    public void setUrlStream(String urlStream) {
        this.urlStream = urlStream;
    }

    @Override
    public String toString() {
        return "{\"MovieDTO\":{"
            + "\"id\":\"" + id + "\""
            + ", \"name\":\"" + name + "\""
            + ", \"genre\":\"" + genre + "\""
            + ", \"urlStream\":\"" + urlStream + "\""
            + "}}";
    }
}
