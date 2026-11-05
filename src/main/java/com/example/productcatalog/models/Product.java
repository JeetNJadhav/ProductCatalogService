package com.example.productcatalog.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Product extends BaseModel{
    private String name;

    private String description;

    private List<ProductImage> images = new ArrayList<>(); // replace it with

    private List<ProductSpecification> specifications = new ArrayList<>();

    private BigDecimal price;

    private String brand;

    private String slug;

    private Category category;

    // this field is added to learn DTO concept
    private Boolean isPrimeSaleDiscountSpecific; // sensitive field
}
