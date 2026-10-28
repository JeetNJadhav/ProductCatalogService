package com.example.productcatalog.models;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
public class BaseModel {
    private Long id;

    private Instant createdAt;

    private LocalDateTime lastUpdatedAt;

    private State state;
}
