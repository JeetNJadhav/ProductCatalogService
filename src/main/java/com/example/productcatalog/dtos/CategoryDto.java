package com.example.productcatalog.dtos;

import com.example.productcatalog.models.Category;
import com.example.productcatalog.models.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryDto {
    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private String slug;
}
