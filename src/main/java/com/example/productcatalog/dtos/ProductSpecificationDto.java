package com.example.productcatalog.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSpecificationDto {
    private Long id;
    private String name;
    private String value;
    private String groupName;
    private int displayOrder;
}
