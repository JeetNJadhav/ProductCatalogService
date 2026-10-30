package com.example.productcatalog.services;

import com.example.productcatalog.dtos.FakeStoreProductDto;
import com.example.productcatalog.models.Category;
import com.example.productcatalog.models.Product;
import com.example.productcatalog.models.ProductImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService implements IProductService{

    private final RestTemplate restTemplate;

    public ProductService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Product getProductById(Long id){
        // FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject
        // ("https://fakestoreapi.com/products{id}", FakeStoreProductDto.class, id);

        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products/{id}", FakeStoreProductDto.class, id);

        if(fakeStoreProductDtoResponseEntity.hasBody() && fakeStoreProductDtoResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200))){
            return from(fakeStoreProductDtoResponseEntity.getBody());
        }

        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        ResponseEntity<FakeStoreProductDto[]> fakeStoreProductsDtoResponseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products", FakeStoreProductDto[].class);
        List<Product> products = new ArrayList<>();
        if(fakeStoreProductsDtoResponseEntity.hasBody() && fakeStoreProductsDtoResponseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200))){
            for(FakeStoreProductDto dto: fakeStoreProductsDtoResponseEntity.getBody()){
                products.add(from(dto));
            }
        }

        return products;
    }

    private Product from(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        ProductImage productImage = new ProductImage();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());

        productImage.setUrl(fakeStoreProductDto.getImage());
        productImage.setAltText(fakeStoreProductDto.getTitle());
        productImage.setPrimary(true);

        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        product.getImages().add(productImage);
        return product;
    }
}
