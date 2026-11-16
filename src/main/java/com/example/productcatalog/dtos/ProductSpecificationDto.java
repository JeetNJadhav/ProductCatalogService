package com.example.productcatalog.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductSpecificationDto {
    private UUID id;
    private String name;
    private String value;
    private String groupName;
    private int displayOrder;
}
