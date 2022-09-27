package com.digitalhouse.catalogservice.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document
public class SerieDTO {


        @Id
        private String id;
        private String name;
        private String genre;
        private Set<Season> seasons;
}
