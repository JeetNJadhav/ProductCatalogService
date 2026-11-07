package com.example.productcatalog.mapper;

import com.example.productcatalog.dtos.*;
import com.example.productcatalog.models.Category;
import com.example.productcatalog.models.Product;
import com.example.productcatalog.models.ProductImage;
import com.example.productcatalog.models.ProductSpecification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProductMapper {

    public Product toEntity(ProductDto productDto) {
        Product product = new Product();

        product.setId(new UUID(0L, productDto.getId()));
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setBrand(productDto.getBrand());
        product.setSlug(productDto.getSlug());
        product.setDescription(productDto.getDescription());

        // Map Images
        List<ProductImage> images = new ArrayList<>();

        for (ProductImageDto imageDto : productDto.getImages()) {
            ProductImage image = new ProductImage();

            image.setId(new UUID(0L, imageDto.getId()));
            image.setUrl(imageDto.getUrl());
            image.setAltText(imageDto.getAltText());
            image.setDisplayOrder(imageDto.getDisplayOrder());
            image.setPrimary(imageDto.isPrimary());

            images.add(image);
        }

        product.setImages(images);

        // Map Specifications
        List<ProductSpecification> specifications = new ArrayList<>();

        for (ProductSpecificationDto specDto : productDto.getSpecifications()) {
            ProductSpecification spec = new ProductSpecification();

            spec.setId(new UUID(0L, specDto.getId()));
            spec.setName(specDto.getName());
            spec.setValue(specDto.getValue());
            spec.setGroupName(specDto.getGroupName());
            spec.setDisplayOrder(specDto.getDisplayOrder());

            specifications.add(spec);
        }

        product.setSpecifications(specifications);

        // Map Categories
        if (productDto.getCategories() != null) {
            for (CategoryDto categoryDto : productDto.getCategories()) {
                Category category = new Category();

                category.setId(new UUID(0L, categoryDto.getId()));
                category.setName(categoryDto.getName());
                category.setDescription(categoryDto.getDescription());
                category.setImageUrl(categoryDto.getImageUrl());
                category.setSlug(categoryDto.getSlug());

                product.getCategories().add(category);
            }
        }

        return product;
    }

    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId().getLeastSignificantBits());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setBrand(product.getBrand());
        productDto.setSlug(product.getSlug());

        productDto.setImages(toImageDtos(product.getImages()));
        productDto.setSpecifications(toSpecDtos(product.getSpecifications()));

        if (product.getCategories() != null) {
            List<CategoryDto> categoryDtos = new ArrayList<>();

            for (Category category : product.getCategories()) {
                CategoryDto categoryDto = new CategoryDto();

                categoryDto.setId(category.getId().getLeastSignificantBits());
                categoryDto.setName(category.getName());
                categoryDto.setDescription(category.getDescription());

                categoryDtos.add(categoryDto);
            }
            productDto.setCategories(categoryDtos);
        }
        return productDto;
    }

    private List<ProductImageDto> toImageDtos(List<ProductImage> images) {
        List<ProductImageDto> imageDtos = new ArrayList<>();
        for (ProductImage image : images) {
            ProductImageDto imageDto = new ProductImageDto();
            imageDto.setId(image.getId().getLeastSignificantBits());
            imageDto.setUrl(image.getUrl());
            imageDto.setAltText(image.getAltText());
            imageDto.setDisplayOrder(image.getDisplayOrder());
            imageDto.setPrimary(image.isPrimary());
            imageDtos.add(imageDto);
        }
        return imageDtos;
    }

    private List<ProductSpecificationDto> toSpecDtos(List<ProductSpecification> specs) {
        List<ProductSpecificationDto> specDtos = new ArrayList<>();
        for (ProductSpecification spec : specs) {
            ProductSpecificationDto specDto = new ProductSpecificationDto();
            specDto.setId(spec.getId().getLeastSignificantBits());
            specDto.setName(spec.getName());
            specDto.setValue(spec.getValue());
            specDto.setGroupName(spec.getGroupName());
            specDto.setDisplayOrder(spec.getDisplayOrder());
            specDtos.add(specDto);
        }
        return specDtos;
    }

    public FakeStoreProductDto toFakeStoreDto(Product product) {

        FakeStoreProductDto dto = new FakeStoreProductDto();

        dto.setId(product.getId().getLeastSignificantBits());
        dto.setTitle(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());

        if (product.getCategories() != null) {
            dto.setCategory(product.getCategories().get(0).getName());
        }

        if (product.getImages() != null && !product.getImages().isEmpty()) {
            dto.setImage(product.getImages().get(0).getUrl());
        }

        return dto;
    }

    public Product toProduct(FakeStoreProductDto dto) {

        Product product = new Product();

        product.setId(new UUID(0L, dto.getId()));
        product.setName(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());

        ProductImage productImage = new ProductImage();

        productImage.setUrl(dto.getImage());
        productImage.setAltText(dto.getTitle());
        productImage.setPrimary(true);

        Category category = new Category();
        category.setName(dto.getCategory());

        product.getCategories().add(category);
        product.getImages().add(productImage);

        return product;
    }
}
