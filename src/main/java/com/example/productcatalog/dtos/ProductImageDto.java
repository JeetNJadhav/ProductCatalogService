package com.example.productcatalog.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageDto {
    private Long id;
    private String url;
    private String altText;
    private int displayOrder;
    private boolean primary;
}
