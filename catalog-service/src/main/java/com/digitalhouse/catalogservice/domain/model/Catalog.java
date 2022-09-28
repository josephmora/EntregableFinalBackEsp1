package com.digitalhouse.catalogservice.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Getter
@Setter
@Document("Catalogo")
public class Catalog {
    @Id
    private String id;
    private List<Movie> movies;
    private List<Serie> series;

    public Catalog() {
    }
}
