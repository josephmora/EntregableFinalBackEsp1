package com.digitalhouse.catalogservice.api.repository;

import com.digitalhouse.catalogservice.domain.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
}
