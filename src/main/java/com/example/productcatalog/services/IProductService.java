package com.example.productcatalog.services;

import com.example.productcatalog.dtos.ProductDto;

import java.util.List;

/**
 * Strategy contract for product data access.
 *
 * The controller depends only on this interface. Exactly one implementation is
 * registered at startup, selected by the {@code catalog.product-source} property:
 * <ul>
 *     <li>{@code db} (default) - {@link DbProductService}, backed by MySQL (source of truth)</li>
 *     <li>{@code fakestore} - FSProductService, backed by the FakeStore API (temporary)</li>
 * </ul>
 *
 * IDs are always exchanged as Strings; each implementation converts them to its
 * own native type (UUID for the database, Long for FakeStore).
 */
public interface IProductService {

    ProductDto getProductById(String id);

    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsById(List<String> productIds);

    ProductDto addProduct(ProductDto product);

    ProductDto replaceProduct(String id, ProductDto product);

    void deleteProductById(String id);
}
