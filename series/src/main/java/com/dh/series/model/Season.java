package com.dh.series.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
@Data
public class Season {
    @Id
    private String id;
    private String seasonNumber;
    private Set<Chapter> chapters;
}
