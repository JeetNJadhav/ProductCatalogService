package com.example.productcatalog.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSpecification extends BaseModel{
    private String name;
    private String value;
    private String groupName;      // "Memory", "Display", "Battery" (optional)
    private int displayOrder;
    private Product product;
}
