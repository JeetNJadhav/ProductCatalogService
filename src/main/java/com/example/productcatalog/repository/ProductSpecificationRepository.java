package com.example.productcatalog.repository;


import com.example.productcatalog.models.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.UUID;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, UUID> {
    List<ProductSpecification> findByProductId(UUID productId);
}
