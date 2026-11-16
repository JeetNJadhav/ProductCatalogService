package com.example.productcatalog.integration.fakestore.service;

import com.example.productcatalog.dtos.ProductDto;
import com.example.productcatalog.exception.InvalidProductIdException;
import com.example.productcatalog.exception.ProductNotFoundException;
import com.example.productcatalog.exception.ProductSourceUnavailableException;
import com.example.productcatalog.integration.fakestore.client.FakeStoreApiClient;
import com.example.productcatalog.integration.fakestore.dto.FakeStoreProductDto;
import com.example.productcatalog.integration.fakestore.mapper.FakeStoreMapper;
import com.example.productcatalog.services.IProductService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

/**
 * FakeStore API implementation. Temporary: exists to demonstrate a third-party
 * integration and will be removed together with the integration/fakestore package.
 * Active only when catalog.product-source=fakestore.
 */
@Service
@ConditionalOnProperty(
        name = "catalog.product-source",
        havingValue = "fakestore"
)
public class FSProductService implements IProductService {

    private final FakeStoreApiClient fakeStoreApiClient;
    private final FakeStoreMapper fakeStoreMapper;

    public FSProductService(FakeStoreApiClient fakeStoreApiClient, FakeStoreMapper fakeStoreMapper) {
        this.fakeStoreApiClient = fakeStoreApiClient;
        this.fakeStoreMapper = fakeStoreMapper;
    }

    @Override
    public ProductDto getProductById(String id) {
        Long productId = toLong(id);

        FakeStoreProductDto fakeStoreProductDto;
        try {
            fakeStoreProductDto = fakeStoreApiClient.getProductById(productId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProductNotFoundException(productId);
        } catch (RestClientException e) {
            throw unavailable(e);
        }

        if (fakeStoreProductDto == null) {
            throw new ProductNotFoundException(productId);
        }

        return fakeStoreMapper.toDto(fakeStoreProductDto);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        FakeStoreProductDto[] fakeStoreProductDtos;
        try {
            fakeStoreProductDtos = fakeStoreApiClient.getAllProducts();
        } catch (RestClientException e) {
            throw unavailable(e);
        }

        List<ProductDto> result = new ArrayList<>();
        for (FakeStoreProductDto product : fakeStoreProductDtos) {
            result.add(fakeStoreMapper.toDto(product));
        }
        return result;
    }

    /**
     * FakeStore has no batch endpoint, so each product is fetched individually.
     * Missing IDs are skipped, matching the database implementation (findAllById).
     */
    @Override
    public List<ProductDto> getAllProductsById(List<String> productIds) {
        // Validate every ID before making any remote call
        List<Long> ids = new ArrayList<>();
        for (String productId : productIds) {
            ids.add(toLong(productId));
        }

        List<ProductDto> result = new ArrayList<>();
        for (Long id : ids) {
            try {
                FakeStoreProductDto fakeStoreProductDto = fakeStoreApiClient.getProductById(id);
                if (fakeStoreProductDto != null) {
                    result.add(fakeStoreMapper.toDto(fakeStoreProductDto));
                }
            } catch (HttpClientErrorException.NotFound e) {
                // skip missing product
            } catch (RestClientException e) {
                throw unavailable(e);
            }
        }
        return result;
    }

    @Override
    public ProductDto addProduct(ProductDto product) {
        FakeStoreProductDto created;
        try {
            created = fakeStoreApiClient.createProduct(fakeStoreMapper.toFakeStoreDto(product));
        } catch (RestClientException e) {
            throw unavailable(e);
        }

        if (created == null) {
            throw new ProductSourceUnavailableException("FakeStore did not return the created product", null);
        }

        return fakeStoreMapper.toDto(created);
    }

    @Override
    public ProductDto replaceProduct(String id, ProductDto product) {
        Long productId = toLong(id);

        FakeStoreProductDto replaced;
        try {
            replaced = fakeStoreApiClient.replaceProduct(productId, fakeStoreMapper.toFakeStoreDto(product));
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProductNotFoundException(productId);
        } catch (RestClientException e) {
            throw unavailable(e);
        }

        if (replaced == null) {
            throw new ProductNotFoundException(productId);
        }

        return fakeStoreMapper.toDto(replaced);
    }

    @Override
    public void deleteProductById(String id) {
        Long productId = toLong(id);

        FakeStoreProductDto deleted;
        try {
            deleted = fakeStoreApiClient.deleteProduct(productId);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProductNotFoundException(productId);
        } catch (RestClientException e) {
            throw unavailable(e);
        }

        if (deleted == null) {
            throw new ProductNotFoundException(productId);
        }
    }

    private Long toLong(String id) {
        if (id == null) {
            throw new InvalidProductIdException(null);
        }
        long productId;
        try {
            productId = Long.parseLong(id.trim());
        } catch (NumberFormatException e) {
            throw new InvalidProductIdException(id);
        }
        if (productId <= 0) {
            throw new InvalidProductIdException(id);
        }
        return productId;
    }

    private ProductSourceUnavailableException unavailable(RestClientException e) {
        return new ProductSourceUnavailableException("FakeStore API request failed", e);
    }
}
