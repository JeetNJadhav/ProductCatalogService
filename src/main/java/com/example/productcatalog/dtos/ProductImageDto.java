package com.example.productcatalog.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductImageDto {
    private UUID id;
    private String url;
    private String altText;
    private int displayOrder;
    private boolean primary;
}
