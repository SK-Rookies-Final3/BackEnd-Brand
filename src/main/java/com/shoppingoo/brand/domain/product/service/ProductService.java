package com.shoppingoo.brand.domain.product.service;

import com.shoppingoo.brand.db.product.enums.Category;
import com.shoppingoo.brand.domain.product.dto.ProductRequest;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;

import java.util.List;


public interface ProductService {

    ProductResponse productRegister(int storeId, int userId, String thumbnailUrl, ProductRequest productRequest);
    ProductResponse productUpdate(int storeId, int productCode, String thumbnailUrl, ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductByCode(int productCode);
    List<ProductResponse> getProductByCategory(Category category);
    void productDelete(int storeId, int productCode);
}
