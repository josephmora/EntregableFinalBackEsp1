package com.digitalhouse.catalogservice.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CatalogDTO {
    List<MovieDTO> movies;
    List<SerieDTO> series;

    public CatalogDTO() {
    }
}
