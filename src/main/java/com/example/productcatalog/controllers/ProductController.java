package com.example.productcatalog.controllers;

import com.example.productcatalog.models.Category;
import com.example.productcatalog.models.Product;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    List<Product> list = new ArrayList<>();
    @GetMapping("/products")
    public List<Product> getAllProducts(){
        Product product = new Product();
        Category category = new Category();
        product.setId(1L);
        product.setName("Iphone");
        category.setName("Mobile");
        product.setCategory(category);
        product.setDescription("New Ios 18");
        product.setPrice(100.0);

        list.add(product);
        return list;
    }

    @GetMapping("/product/{id}")
    public Product getProductByid(@PathVariable("id") Long productId){
        Product product = new Product();
        product.setId(productId);
        return product;
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product product){
        list.add(product);
        return product;
    }

}
