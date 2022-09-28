package com.digitalhouse.catalogservice.api.service;

import java.util.ArrayList;
import java.util.List;

import com.digitalhouse.catalogservice.api.client.SerieClient;
import com.digitalhouse.catalogservice.api.repository.CatalogRepository;
import com.digitalhouse.catalogservice.api.repository.MovieRepository;
import com.digitalhouse.catalogservice.api.repository.SerieRepository;
import com.digitalhouse.catalogservice.domain.model.Catalog;
import com.digitalhouse.catalogservice.domain.model.Serie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.digitalhouse.catalogservice.api.client.MovieClient;
import com.digitalhouse.catalogservice.domain.model.Movie;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

@Service
public class CatalogService {


    private final Logger LOG = LoggerFactory.getLogger(CatalogService.class);

    private final MovieClient movieClient;
    private final SerieClient serieClient;
    private final MovieRepository movieRepository;
    private final SerieRepository serieRepository;
    private final CatalogRepository catalogRepository;

    @Autowired
    public CatalogService(MovieClient movieClient, SerieClient serieClient, MovieRepository movieRepository, SerieRepository serieRepository, CatalogRepository catalogRepository) {
        this.movieClient = movieClient;
        this.serieClient = serieClient;
        this.movieRepository = movieRepository;
        this.serieRepository = serieRepository;

        this.catalogRepository = catalogRepository;
    }

    public Catalog findCatalogByGenre(String genre) {
        LOG.info("Se va a incluir el llamado al movie-service...");
        List<Movie> listMovies= movieClient.getMovieByGenre(genre);
        List<Serie> listSeries= serieClient.getSerieByGenre(genre);
        Catalog catalog = new Catalog();
        catalog.setMovies(listMovies);
        catalog.setSeries(listSeries);
        //persisto las peliculas y series obtenidad del microservicio movies
        catalogRepository.save(catalog);

        return catalog;
    }



    @CircuitBreaker(name = "movies", fallbackMethod = "moviesFallbackMethod")
    public ResponseEntity<List<Movie>> findMovieByGenre(String genre, Boolean throwError) {
        LOG.info("Se va a incluir el llamado al movie-service...");

        return movieClient.getMovieByGenreWithThrowError(genre, throwError);
    }

    private ResponseEntity<List<Movie>> moviesFallbackMethod(CallNotPermittedException exception) {
        LOG.info("se activó el circuitbreaker");
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }


    @RabbitListener(queues = "${queue.movie}")
    public void save(Movie movie) {
        LOG.info("Se recibio una movie a través de rabbit " + movie.toString());
        saveMovie(movie);

    }

    @RabbitListener(queues = "${queue.serie}")
    public void save(Serie serie) {
        LOG.info("Se recibio una serie a través de rabbit " + serie.toString());
        saveSerie(serie);

    }


    //traer datos persistidos
    public List<Movie> findAllMovies() {
        LOG.info("se va a buscar lista de peliculas persistidas con rabbit en catalogo");
        return movieRepository.findAll();
    }

    public List<Serie> findAllSeries() {
        LOG.info("se va a buscar lista de series persistidas con rabbit en catalogo");
        return serieRepository.findAll();
    }

    public void saveMovie(Movie movie){
        movieRepository.save(movie);
    }

    public void saveSerie(Serie serie){
        serieRepository.save(serie);
    }
}
