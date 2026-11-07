package com.example.productcatalog.client;

import com.example.productcatalog.dtos.FakeStoreProductDto;
import com.example.productcatalog.models.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.lang.Nullable;

@Component
public class FakeStoreApiClient {

    private final RestTemplate restTemplate;

    public FakeStoreApiClient(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }

    private <T> ResponseEntity<T> requestForEntity(HttpMethod httpMethod, String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {

        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    private <T> Boolean validateResponse(ResponseEntity<T> fakeStoreProductDtoResponseEntity) {
        if (fakeStoreProductDtoResponseEntity.hasBody() &&
                fakeStoreProductDtoResponseEntity.getStatusCode()
                        .equals(HttpStatusCode.valueOf(200))) {
            return true;
        }

        return false;
    }

    public FakeStoreProductDto getProductById(Long id) {
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = requestForEntity(
                HttpMethod.GET,"https://fakestoreapi.com/products/{id}", null,
                FakeStoreProductDto.class, id);

        if(validateResponse(fakeStoreProductDtoResponseEntity)) {
            return fakeStoreProductDtoResponseEntity.getBody();
        }

        return null;
    }

    public FakeStoreProductDto[] getAllProducts() {

        ResponseEntity<FakeStoreProductDto[]> response =
                requestForEntity(
                        HttpMethod.GET,
                        "https://fakestoreapi.com/products",
                        null,
                        FakeStoreProductDto[].class
                );

        if (validateResponse(response)) {
            return response.getBody();
        }

        return new FakeStoreProductDto[0];
    }

    public FakeStoreProductDto replaceProduct(Long id, FakeStoreProductDto fakeStoreProductDtoInput){
        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = requestForEntity(HttpMethod.PUT,
                "https://fakestoreapi.com/products/{id}",
                fakeStoreProductDtoInput, FakeStoreProductDto.class, id);

        if(validateResponse(fakeStoreProductDtoResponseEntity)) {
            return fakeStoreProductDtoResponseEntity.getBody();
        }

        return null;
    }

}
