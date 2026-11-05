package com.example.productcatalog.services;

import com.example.productcatalog.client.FakeStoreApiClient;
import com.example.productcatalog.dtos.FakeStoreProductDto;
import com.example.productcatalog.mapper.ProductMapper;
import com.example.productcatalog.models.Category;
import com.example.productcatalog.models.Product;
import com.example.productcatalog.models.ProductImage;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;


import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService implements IProductService{

    private final RestTemplate restTemplate;
//    @Autowired
//    private RestTemplateBuilder restTemplateBuilder;
    private FakeStoreApiClient fakeStoreApiClient;
    private final ProductMapper productMapper;

    public ProductService(RestTemplateBuilder restTemplateBuilder, FakeStoreApiClient fakeStoreApiClient,ProductMapper productMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.fakeStoreApiClient = fakeStoreApiClient;
        this.productMapper = productMapper;
    }

    @Override
    public Product getProductById(Long id){
        // FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject
        // ("https://fakestoreapi.com/products{id}", FakeStoreProductDto.class, id);

        FakeStoreProductDto fakeStoreProductDto = fakeStoreApiClient.getProductById(id);

        if(fakeStoreProductDto == null){
            return null;
        }

        return productMapper.toProduct(fakeStoreProductDto);
    }

    @Override
    public List<Product> getAllProducts() {

        FakeStoreProductDto[] fakeStoreProductDtos =
                fakeStoreApiClient.getAllProducts();

        List<Product> products = new ArrayList<>();

        for (FakeStoreProductDto dto : fakeStoreProductDtos) {
            products.add(productMapper.toProduct(dto));
        }

        return products;
    }

    @Override
    public Product replaceProduct(Long id, Product inputProduct) {
        FakeStoreProductDto fakeStoreProductDtoInput = productMapper.toFakeStoreDto(inputProduct);
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = requestForEntity(HttpMethod.PUT,
                "https://fakestoreapi.com/products/{id}",
                fakeStoreProductDtoInput, FakeStoreProductDto.class, id);

        if (fakeStoreProductDtoResponseEntity.hasBody() &&
                fakeStoreProductDtoResponseEntity.getStatusCode()
                        .equals(HttpStatusCode.valueOf(200))) {
            return productMapper.toProduct(fakeStoreProductDtoResponseEntity.getBody());
        }

        return null;
    }

    private <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod, String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
//        RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }
}
