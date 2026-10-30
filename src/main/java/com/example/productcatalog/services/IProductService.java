package com.example.productcatalog.services;

import com.example.productcatalog.models.Product;

import java.util.List;

public interface IProductService {
    Product getProductById(Long id);
    List<Product> getAllProducts();
}
