package com.example.productcatalog.controllers;

import com.example.productcatalog.dtos.ProductDto;
import com.example.productcatalog.services.IProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//singleton object -> bean
//container (spring context)  in which beans are stored

/**
 * Single REST entry point for products. Depends only on IProductService, so it does
 * not know (or care) whether MySQL or FakeStore is the active source. IDs are Strings;
 * the active service converts and validates them.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private static final int MAX_BATCH_SIZE = 100;

    // Constructor injection: Spring injects the one IProductService bean that matched
    // catalog.product-source at startup.
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("productId") String productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @PostMapping("/by-ids")
    public ResponseEntity<List<ProductDto>> getAllProductsById(@RequestBody List<String> productIds) {

        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        if (productIds.size() > MAX_BATCH_SIZE) {
            throw new IllegalArgumentException("Batch size exceeds limit of " + MAX_BATCH_SIZE);
        }

        return ResponseEntity.ok(productService.getAllProductsById(productIds));
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.addProduct(productDto), HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> replaceProduct(
            @PathVariable("productId") String productId,
            @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.replaceProduct(productId, productDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable("productId") String productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }
}
