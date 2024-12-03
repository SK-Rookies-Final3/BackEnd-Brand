package com.shoppingoo.brand.domain.product.service;

import com.shoppingoo.brand.db.product.enums.Category;
import com.shoppingoo.brand.domain.product.dto.ProductAllResponse;
import com.shoppingoo.brand.domain.product.dto.ProductRequest;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
import reactor.core.publisher.Mono;

import java.util.List;


public interface ProductService {

    Mono<ProductResponse> productRegister(int storeId, int userId, ProductRequest productRequest, List<String> thumbnailFileNames, List<String> imageFileNames);
    Mono<ProductResponse> productUpdate(int storeId, int userId, int productCode, ProductRequest productRequest, List<String> thumbnailFileNames, List<String> imageFileNames);
    List<ProductResponse> getAllProducts();
    ProductAllResponse getProductByCode(int productCode);
    List<ProductResponse> getProductByCategory(Category category);
    void productDelete(int storeId, int userId, int productCode);
    List<ProductResponse> getProductByStoreId(int storeId);
    List<ProductResponse> getProductByUserId(int userId);
    int getProductStock(int productCode);
}
