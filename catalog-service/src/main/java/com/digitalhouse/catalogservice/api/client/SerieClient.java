package com.digitalhouse.catalogservice.api.client;

import com.digitalhouse.catalogservice.domain.model.Serie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name= "series-service")
public interface SerieClient {
    @GetMapping("/series/{genre}")
    List<Serie> getSerieByGenre(@PathVariable(value = "genre")String genre);

}
