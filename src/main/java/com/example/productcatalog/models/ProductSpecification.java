package com.example.productcatalog.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductSpecification extends BaseModel{
    private String name;
    private String value;
    private String groupName;      // "Memory", "Display", "Battery" (optional)
    private int displayOrder;

    @ManyToOne
    private Product product;
}
