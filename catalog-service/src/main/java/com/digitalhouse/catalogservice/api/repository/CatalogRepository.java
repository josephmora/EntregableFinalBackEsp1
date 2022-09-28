package com.digitalhouse.catalogservice.api.repository;

import com.digitalhouse.catalogservice.domain.model.Catalog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CatalogRepository extends MongoRepository<Catalog, String> {

}
