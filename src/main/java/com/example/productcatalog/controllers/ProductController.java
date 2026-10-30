package com.example.productcatalog.controllers;

import com.example.productcatalog.dtos.CategoryDto;
import com.example.productcatalog.dtos.ProductDto;
import com.example.productcatalog.dtos.ProductImageDto;
import com.example.productcatalog.dtos.ProductSpecificationDto;
import com.example.productcatalog.models.Category;
import com.example.productcatalog.models.Product;
import com.example.productcatalog.models.ProductImage;
import com.example.productcatalog.models.ProductSpecification;
import com.example.productcatalog.services.IProductService;
import com.example.productcatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//singleton object -> bean
//container (spring context)  in which beans are stored

@RestController
public class ProductController {

// Field injection (not recommended): dependency is hidden, can't be final, hard to unit test
// @Autowired
// private IProductService productService;

    // Constructor injection (recommended): explicit, final/immutable, easy to test, fails fast
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(from(product));
        }

        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        Product product = productService.getProductById(productId);
//        MultiValueMap deprecated so using httpHeaders
//        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("called by","Jeet Jadhav");

        if (product == null) {
            return new ResponseEntity<>(null,headers, HttpStatus.NOT_FOUND);
        }

        ProductDto productDto = from(product);
        return new ResponseEntity<>(productDto, headers, HttpStatus.OK);
    }

    @PostMapping("/products")
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productDto;
    }

    private ProductDto from(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setBrand(product.getBrand());
        productDto.setSlug(product.getSlug());

        productDto.setImages(toImageDtos(product.getImages()));
        productDto.setSpecifications(toSpecDtos(product.getSpecifications()));

        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setDescription(product.getCategory().getDescription());
            productDto.setCategory(categoryDto);
        }
        return productDto;
    }

    private List<ProductImageDto> toImageDtos(List<ProductImage> images) {
        List<ProductImageDto> imageDtos = new ArrayList<>();
        for (ProductImage image : images) {
            ProductImageDto imageDto = new ProductImageDto();
            imageDto.setId(image.getId());
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
            specDto.setId(spec.getId());
            specDto.setName(spec.getName());
            specDto.setValue(spec.getValue());
            specDto.setGroupName(spec.getGroupName());
            specDto.setDisplayOrder(spec.getDisplayOrder());
            specDtos.add(specDto);
        }
        return specDtos;
    }
}
