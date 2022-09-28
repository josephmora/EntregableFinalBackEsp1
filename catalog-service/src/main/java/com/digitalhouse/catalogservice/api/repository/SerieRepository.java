package com.digitalhouse.catalogservice.api.repository;

import com.digitalhouse.catalogservice.domain.model.Serie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SerieRepository extends MongoRepository<Serie, String> {
}
