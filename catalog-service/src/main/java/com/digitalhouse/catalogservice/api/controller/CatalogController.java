package com.digitalhouse.catalogservice.api.controller;

import java.util.List;

import com.digitalhouse.catalogservice.domain.model.Catalog;
import com.digitalhouse.catalogservice.domain.model.Serie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.digitalhouse.catalogservice.api.service.CatalogService;
import com.digitalhouse.catalogservice.domain.model.Movie;

@RestController
@RequestMapping("/catalogs")
public class CatalogController {

    private final CatalogService catalogService;

    @Autowired
    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/{genre}")
    public ResponseEntity<Catalog> getGenre(@PathVariable String genre) {

        return ResponseEntity.ok().body(catalogService.findCatalogByGenre(genre));
    }

    @GetMapping("/withErrors/{genre}")
    public ResponseEntity<List<Movie>> getGenre(@PathVariable String genre, @RequestParam("throwError") Boolean throwError) {
        return catalogService.findMovieByGenre(genre, throwError);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveMovie(@RequestBody Movie movie) {
        catalogService.saveMovie(movie);
        return ResponseEntity.ok("Se guardo una pelicula");
    }
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovieQueue(){
        return ResponseEntity.ok().body( catalogService.findAllMovies());
    }
    @GetMapping
    public ResponseEntity<List<Serie>> getAllSerieQueue(){
        return ResponseEntity.ok().body( catalogService.findAllSeries());
    }

}
