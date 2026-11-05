package com.example.productcatalog.dtos;

import com.example.productcatalog.models.Category;
import com.example.productcatalog.models.ProductImage;
import com.example.productcatalog.models.ProductSpecification;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductDto {
    private Long id;

    private String name;

    private String description;

    private List<ProductImageDto> images = new ArrayList<>(); // replace it with

    private List<ProductSpecificationDto> specifications = new ArrayList<>();

    private BigDecimal price;

    private String brand;

    private String slug;

    private CategoryDto category;
}
