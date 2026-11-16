package com.example.productcatalog.integration.fakestore.client;

import com.example.productcatalog.integration.fakestore.dto.FakeStoreProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Thin HTTP client for the FakeStore API. Only talks HTTP and FakeStore DTOs;
 * mapping and error translation live in FSProductService.
 */
@Component
@ConditionalOnProperty(name = "catalog.product-source", havingValue = "fakestore")
public class FakeStoreApiClient {

    private final RestTemplate restTemplate;

    public FakeStoreApiClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${fakestore.base-url:https://fakestoreapi.com}") String baseUrl
    ) {
        this.restTemplate = restTemplateBuilder
                .rootUri(baseUrl)
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(10))
                .build();
    }

    private <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod, String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {

        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    private <T> boolean validateResponse(@Nullable ResponseEntity<T> responseEntity) {
        return responseEntity != null
                && responseEntity.hasBody()
                && responseEntity.getStatusCode().is2xxSuccessful();
    }

    /** Returns null when FakeStore has no product with this ID (it answers 200 with an empty body). */
    public FakeStoreProductDto getProductById(Long id) {
        ResponseEntity<FakeStoreProductDto> response = requestForEntity(
                HttpMethod.GET, "/products/{id}", null,
                FakeStoreProductDto.class, id);

        if (validateResponse(response)) {
            return response.getBody();
        }

        return null;
    }

    public FakeStoreProductDto[] getAllProducts() {
        ResponseEntity<FakeStoreProductDto[]> response = requestForEntity(
                HttpMethod.GET, "/products", null,
                FakeStoreProductDto[].class);

        if (validateResponse(response)) {
            return response.getBody();
        }

        return new FakeStoreProductDto[0];
    }

    public FakeStoreProductDto createProduct(FakeStoreProductDto input) {
        ResponseEntity<FakeStoreProductDto> response = requestForEntity(
                HttpMethod.POST, "/products", input,
                FakeStoreProductDto.class);

        if (validateResponse(response)) {
            return response.getBody();
        }

        return null;
    }

    public FakeStoreProductDto replaceProduct(Long id, FakeStoreProductDto input) {
        ResponseEntity<FakeStoreProductDto> response = requestForEntity(
                HttpMethod.PUT, "/products/{id}", input,
                FakeStoreProductDto.class, id);

        if (validateResponse(response)) {
            return response.getBody();
        }

        return null;
    }

    /** Returns the deleted product, or null when FakeStore has no product with this ID. */
    public FakeStoreProductDto deleteProduct(Long id) {
        ResponseEntity<FakeStoreProductDto> response = requestForEntity(
                HttpMethod.DELETE, "/products/{id}", null,
                FakeStoreProductDto.class, id);

        if (validateResponse(response)) {
            return response.getBody();
        }

        return null;
    }
}
