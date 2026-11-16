package com.example.productcatalog.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Object id) {
        super("Product Not found with ID: " + id);
    }
}
