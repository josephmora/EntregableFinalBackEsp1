package com.digitalhouse.movieservice.api.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.digitalhouse.movieservice.domain.models.Movie;
import com.digitalhouse.movieservice.domain.models.MovieInfo;
import com.digitalhouse.movieservice.domain.repositories.MovieRepository;
import com.digitalhouse.movieservice.util.RedisUtils;

@Service
public class MovieService {

    private static final Logger LOG = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository repository;
    private final RedisUtils redisUtils;

    @Autowired
    public MovieService(MovieRepository movieRepository, RedisUtils redisUtils) {
        this.repository = movieRepository;
        this.redisUtils = redisUtils;
    }

    public List<Movie> findByGenre(String genre) {
        MovieInfo movieInfo = redisUtils.getMovieInfo(genre);
        if (Objects.nonNull(movieInfo)) {
            return movieInfo.getMovies();
        }
        List<Movie> movies = repository.findByGenre(genre);
        redisUtils.createMovieInfo(genre, movies);
        return movies;
    }

    public List<Movie> findByGenre(String genre, Boolean throwError) {
        LOG.info("se van a buscar las peliculas por género");
        if (throwError) {
            LOG.error("Hubo un error al buscar las películas");
            throw new RuntimeException();
        }
        return repository.findByGenre(genre);
    }

    @RabbitListener(queues = "${queue.movie.name}")
    public void save(Movie movie) {
        LOG.info("Se recibio una movie a través de rabbit " + movie.toString());
        saveMovie(movie);
    }

    public Movie saveMovie(Movie movie) {
        return repository.save(movie);
    }

}
