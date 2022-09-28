package com.digitalhouse.catalogservice.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
@Data
@Document
public class Season {
    private String id;
    private String seasonNumber;
    private List<Chapter> chapters;
}
