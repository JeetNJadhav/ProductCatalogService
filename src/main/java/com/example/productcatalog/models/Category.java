package com.example.productcatalog.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Category extends BaseModel{
    private String name;

    private String description;

    private String imageUrl;

    private String slug;

    private Category parent;

    private List<Category> children = new ArrayList<>();

    private List<Product> products;
}
