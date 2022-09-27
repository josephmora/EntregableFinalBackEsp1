package com.digitalhouse.movieservice.api.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.digitalhouse.movieservice.domain.models.Movie;
import com.digitalhouse.movieservice.domain.models.MovieInfo;
import com.digitalhouse.movieservice.domain.repositories.MovieRepository;
import com.digitalhouse.movieservice.util.RedisUtils;

@Service
public class MovieService {
    @Value("$queue.movie") //trae el nombre de la cola del booststrap
    private String movieQueue;

    private static final Logger LOG = LoggerFactory.getLogger(MovieService.class);

    private final MovieRepository repository;
    private final RedisUtils redisUtils;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MovieService(MovieRepository movieRepository, RedisUtils redisUtils, RabbitTemplate rabbitTemplate) {
        this.repository = movieRepository;
        this.redisUtils = redisUtils;
        this.rabbitTemplate = rabbitTemplate;
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

    //creo metodo para guardar en cola
    public void saveMovieRabbit(Movie movie){
        rabbitTemplate.convertAndSend(movieQueue, movie);
    }

    public Movie saveMovie(Movie movie) {
        return repository.save(movie);
    }

}
