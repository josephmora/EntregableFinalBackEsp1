package com.digitalhouse.catalogservice.api.service;

import java.util.ArrayList;
import java.util.List;

import com.digitalhouse.catalogservice.api.client.SerieClient;
import com.digitalhouse.catalogservice.api.repository.MovieRepository;
import com.digitalhouse.catalogservice.api.repository.SerieRepository;
import com.digitalhouse.catalogservice.domain.model.CatalogDTO;
import com.digitalhouse.catalogservice.domain.model.SerieDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.digitalhouse.catalogservice.api.client.MovieClient;
import com.digitalhouse.catalogservice.domain.model.MovieDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

@Service
public class CatalogService {


    private final Logger LOG = LoggerFactory.getLogger(CatalogService.class);

    private final MovieClient movieClient;
    private final SerieClient serieClient;
    private final MovieRepository movieRepository;
    private final SerieRepository serieRepository;

    @Autowired
    public CatalogService(MovieClient movieClient, SerieClient serieClient, MovieRepository movieRepository, SerieRepository serieRepository) {
        this.movieClient = movieClient;
        this.serieClient = serieClient;
        this.movieRepository = movieRepository;
        this.serieRepository = serieRepository;
    }

    public CatalogDTO findCatalogByGenre(String genre) {
        LOG.info("Se va a incluir el llamado al movie-service...");
        List<MovieDTO> listMovies= movieClient.getMovieByGenre(genre);
        List<SerieDTO> listSeries= serieClient.getSerieByGenre(genre);
        CatalogDTO catalog = new CatalogDTO();
        catalog.setMovies(listMovies);
        catalog.setSeries(listSeries);
        //persisto las peliculas obtenidad del microservicio movies
        //movieRepository.saveAll(listMovies);

        return catalog;
    }



    @CircuitBreaker(name = "movies", fallbackMethod = "moviesFallbackMethod")
    public ResponseEntity<List<MovieDTO>> findMovieByGenre(String genre, Boolean throwError) {
        LOG.info("Se va a incluir el llamado al movie-service...");

        return movieClient.getMovieByGenreWithThrowError(genre, throwError);
    }

    private ResponseEntity<List<MovieDTO>> moviesFallbackMethod(CallNotPermittedException exception) {
        LOG.info("se activó el circuitbreaker");
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }


    @RabbitListener(queues = "${queue.movie}")
    public void save(MovieDTO movie) {
        LOG.info("Se recibio una movie a través de rabbit " + movie.toString());
        SaveMovie(movie);
        //falta temrinarlo poruqe hay que llamar por feing a series para guardar esta cola a
        //falta persistir estos datos en mongo aqui
    }


    //traer datos persistidos
    public List<MovieDTO> findAllMovies() {
        LOG.info("se va a buscar lista de peliculas persistidas en catalogo");
        return movieRepository.findAll();
    }

    public void SaveMovie(MovieDTO movieDTO){
        movieRepository.save(movieDTO);
    }
}
