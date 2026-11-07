package com.example.productcatalog.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductImage extends BaseModel{
    private String url;
    private String altText;
    private int displayOrder;
    private boolean isPrimary;

    @ManyToOne
    private Product product;
}
