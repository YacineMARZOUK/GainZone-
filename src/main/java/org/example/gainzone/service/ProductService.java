package org.example.gainzone.service;

import org.example.gainzone.dto.request.ProductRequest;
import org.example.gainzone.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse updateProduct(Long id, ProductRequest productRequest);

    void deleteProduct(Long id);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);
}
