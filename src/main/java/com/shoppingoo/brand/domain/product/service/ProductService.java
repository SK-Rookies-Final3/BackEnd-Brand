package com.shoppingoo.brand.domain.product.service;

import com.shoppingoo.brand.domain.product.dto.ProductRequest;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;


public interface ProductService {

    Mono<ProductResponse> productRegister(int storeId, int userId, ProductRequest productRequest, List<String> thumbnailFileNames, List<String> imageFileNames);
    ProductResponse productUpdate(int storeId, int userId, int productCode, ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductByCode(int productCode);
    List<ProductResponse> getProductByCategory(String category);
    void productDelete(int storeId, int userId, int productCode);
}
