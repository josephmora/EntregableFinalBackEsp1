package com.dh.series.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.dh.series.model.Series;

import java.util.List;

@Repository
public interface SeriesRepository extends MongoRepository<Series, String> {
    List<Series> findByGenre(String genre);
}
