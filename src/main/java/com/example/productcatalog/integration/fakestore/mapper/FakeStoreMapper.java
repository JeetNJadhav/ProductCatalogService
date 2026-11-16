package com.example.productcatalog.integration.fakestore.mapper;

import com.example.productcatalog.dtos.ProductDto;
import com.example.productcatalog.dtos.ProductImageDto;
import com.example.productcatalog.integration.fakestore.dto.FakeStoreProductDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps between FakeStore's product shape and the application's ProductDto.
 * FakeStore has a single image and a single category name per product.
 */
@Component
@ConditionalOnProperty(name = "catalog.product-source", havingValue = "fakestore")
public class FakeStoreMapper {

    public ProductDto toDto(FakeStoreProductDto source) {

        ProductDto dto = new ProductDto();

        dto.setId(source.getId() == null
                ? null
                : source.getId().toString());

        dto.setName(source.getTitle());
        dto.setDescription(source.getDescription());
        dto.setPrice(source.getPrice());

        if (source.getImage() != null) {
            ProductImageDto image = new ProductImageDto();
            image.setUrl(source.getImage());
            image.setAltText(source.getTitle());
            image.setPrimary(true);

            List<ProductImageDto> images = new ArrayList<>();
            images.add(image);
            dto.setImages(images);
        }

        // FakeStore categories are plain strings with no ID, so they are not mapped
        // onto CategoryDto (which represents a real catalog category).

        return dto;
    }

    public FakeStoreProductDto toFakeStoreDto(ProductDto source) {

        FakeStoreProductDto dto = new FakeStoreProductDto();

        dto.setTitle(source.getName());
        dto.setPrice(source.getPrice());
        dto.setDescription(source.getDescription());

        if (source.getCategories() != null && !source.getCategories().isEmpty()) {
            dto.setCategory(source.getCategories().get(0).getName());
        }

        if (source.getImages() != null && !source.getImages().isEmpty()) {
            dto.setImage(source.getImages().get(0).getUrl());
        }

        return dto;
    }
}
