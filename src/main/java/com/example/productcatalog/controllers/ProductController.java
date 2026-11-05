package com.example.productcatalog.controllers;

import com.example.productcatalog.dtos.ProductDto;
import com.example.productcatalog.mapper.ProductMapper;
import com.example.productcatalog.models.Product;
import com.example.productcatalog.services.IProductService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//singleton object -> bean
//container (spring context)  in which beans are stored

@RestController
@RequestMapping("/products")
public class ProductController {

    // Field injection (not recommended): dependency is hidden, can't be final, hard to unit test
    // @Autowired
    // private IProductService productService;

    // Constructor injection (recommended): explicit, final/immutable, easy to test, fails fast
    private final IProductService productService;
    private final ProductMapper productMapper;

    public ProductController(IProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(productMapper.toDto(product));
        }

        return new ResponseEntity<>(productDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        if (productId <= 0L) {
            throw new IllegalArgumentException("Please try with productId > 0");
        }
        Product product = productService.getProductById(productId);

        // MultiValueMap deprecated so using httpHeaders
        // MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Called-By","Jeet Jadhav");

        if (product == null) {
            // return new ResponseEntity<>(null,headers, HttpStatus.NOT_FOUND);
            throw  new RuntimeException("Product is not available");
        }

        ProductDto productDto = productMapper.toDto(product);
        return new ResponseEntity<>(productDto, headers, HttpStatus.OK);
    }

    @PostMapping()
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productDto;
    }

    @PutMapping("{id}")
    public ProductDto replaceProduct(@PathVariable Long id, @RequestBody ProductDto input) {
        Product inputProduct = productMapper.toEntity(input);
        Product response = productService.replaceProduct(id, inputProduct);
        if(response == null) return null;
        return productMapper.toDto(response);
    }
}
