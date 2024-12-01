package com.shoppingoo.brand.domain.product.controller;

import com.shoppingoo.brand.db.product.enums.Category;
import com.shoppingoo.brand.domain.product.dto.ProductAllResponse;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
import com.shoppingoo.brand.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/brand/product")
public class ProductOpenApiController {

    private final ProductService productService;

    // 상품 전체 조회
    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> productResponseList = productService.getAllProducts();
        return ResponseEntity.ok(productResponseList); // 응답 반환
    }

    // 상품 상세 조회
    @GetMapping("/{productCode}")
    public ResponseEntity<ProductAllResponse> getProductByCode(@PathVariable int productCode) {
        ProductAllResponse productResponse = productService.getProductByCode(productCode);
        if (productResponse != null) {
            return ResponseEntity.ok(productResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // 상품 카테고리 상세 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductByCategory(@PathVariable("category") Category category) {
        List<ProductResponse> productResponseList = productService.getProductByCategory(category);
        return ResponseEntity.ok(productResponseList);
    }

    // 가게 별 상품 전체 조회
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductResponse>> getProductByStoreId(@PathVariable("storeId") int storeId) {
        List<ProductResponse> productResponseList = productService.getProductByStoreId(storeId);
        return ResponseEntity.ok(productResponseList);
    }
}
