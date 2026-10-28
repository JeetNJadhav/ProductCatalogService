package com.example.productcatalog.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImage extends BaseModel{
    private String url;
    private String altText;
    private int displayOrder;
    private boolean isPrimary;
    private Product product;
}
