package com.digitalhouse.movieservice.util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.digitalhouse.movieservice.domain.models.Movie;
import com.digitalhouse.movieservice.domain.models.MovieInfo;

@Component
public class RedisUtils {

    private static final Logger LOG = LoggerFactory.getLogger(RedisUtils.class);

    private static final long TTL = 20L;

    private final RedisTemplate<String, MovieInfo> movieRedisTemplate;

    @Autowired
    public RedisUtils(RedisTemplate<String, MovieInfo> movieRedisTemplate) {
        this.movieRedisTemplate = movieRedisTemplate;
    }

    public void createMovieInfo(String genre, List<Movie> movies) {
        if (!CollectionUtils.isEmpty(movies)) {
            MovieInfo movieInfo = new MovieInfo();
            movieInfo.setMovies(movies);
            movieInfo.setGenre(genre);
            movieInfo.setCreatedDate(LocalDateTime.now().toString());

            movieRedisTemplate.opsForValue().set(genre.toUpperCase(), movieInfo, TTL, TimeUnit.SECONDS);
            LOG.info("Movie information created on Redis " + genre);
        } else {
            LOG.warn("Movies was empty for genre " + genre);
        }
    }

    public MovieInfo getMovieInfo(String genre) {
        String cacheKey = genre.toUpperCase();
        LOG.info("Movie information was found on Redis " + genre);
        return movieRedisTemplate.opsForValue().get(cacheKey);
    }
}
