package com.example.productcatalog.services;

import com.example.productcatalog.dtos.CategoryDto;
import com.example.productcatalog.dtos.ProductDto;
import com.example.productcatalog.exception.InvalidProductIdException;
import com.example.productcatalog.exception.ProductNotFoundException;
import com.example.productcatalog.mapper.ProductMapper;
import com.example.productcatalog.models.Category;
import com.example.productcatalog.models.Product;
import com.example.productcatalog.repository.CategoryRepository;
import com.example.productcatalog.repository.ProductRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * MySQL-backed implementation and the permanent source of truth.
 * Active when catalog.product-source=db, and also when the property is missing.
 */
@Service
@ConditionalOnProperty(
        name = "catalog.product-source",
        havingValue = "db",
        matchIfMissing = true
)
public class DbProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public DbProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    // Read methods run inside a read-only transaction so the lazy collections
    // (categories, images, specifications) can be loaded while mapping to the DTO.

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(String id) {
        return productMapper.toDto(findProduct(toUuid(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return toDtos(productRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProductsById(List<String> productIds) {
        List<UUID> uuids = new ArrayList<>();
        for (String productId : productIds) {
            uuids.add(toUuid(productId));
        }
        return toDtos(productRepository.findAllById(uuids));
    }

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        Product product = new Product();
        productMapper.copyToEntity(productDto, product);
        product.setCategories(resolveCategories(productDto.getCategories()));

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto replaceProduct(String id, ProductDto productDto) {
        Product product = findProduct(toUuid(id));
        productMapper.copyToEntity(productDto, product);
        product.setCategories(resolveCategories(productDto.getCategories()));

        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProductById(String id) {
        UUID productId = toUuid(id);
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }
        productRepository.deleteById(productId);
    }

    private Product findProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private UUID toUuid(String id) {
        if (id == null) {
            throw new InvalidProductIdException(null);
        }
        try {
            return UUID.fromString(id.trim());
        } catch (IllegalArgumentException e) {
            throw new InvalidProductIdException(id);
        }
    }

    /**
     * Products only link to categories that already exist, so each category in the
     * request is looked up by ID instead of being created.
     */
    private List<Category> resolveCategories(List<CategoryDto> categoryDtos) {
        List<Category> categories = new ArrayList<>();
        if (categoryDtos == null) {
            return categories;
        }
        for (CategoryDto categoryDto : categoryDtos) {
            if (categoryDto.getId() == null) {
                throw new IllegalArgumentException("Category ID is required");
            }
            Category category = categoryRepository.findById(categoryDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Category not found with ID: " + categoryDto.getId()));
            categories.add(category);
        }
        return categories;
    }

    private List<ProductDto> toDtos(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(productMapper.toDto(product));
        }
        return productDtos;
    }
}
