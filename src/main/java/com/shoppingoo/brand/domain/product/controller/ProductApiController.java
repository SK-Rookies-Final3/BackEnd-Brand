package com.shoppingoo.brand.domain.product.controller;

import com.shoppingoo.brand.domain.product.dto.ProductRequest;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
import com.shoppingoo.brand.domain.product.service.ProductService;
import com.shoppingoo.brand.domain.store.dto.StoreRequest;
import com.shoppingoo.brand.domain.store.dto.StoreResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins="http://localhost:3000")
public class ProductApiController {

    private final ProductService productService;

    //상품 등록
    @PostMapping("/owner/{storeId}")
    public ResponseEntity<ProductResponse> productRegister(
            @PathVariable("storeId") int storeId,
            @RequestHeader("X-User-Id") int userId,
            @RequestParam("thumbnail_url") String thumbnailUrl,
            @RequestBody ProductRequest productRequest
    ) {
        ProductResponse productResponse = productService.productRegister(storeId, userId, thumbnailUrl, productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    //상품 수정
    @PatchMapping("owner/{storeId}/{productCode}")
    public ResponseEntity<ProductResponse> productUpdate (
            @PathVariable("storeId") int storeId,
            @RequestHeader("X-User-Id") int userId,
            @PathVariable("productCode") int productCode,
            @RequestParam("thumbnail_url") String thumbnailUrl,
            @RequestBody ProductRequest productRequest
    ) {
        ProductResponse productResponse = productService.productUpdate(storeId, userId, productCode, thumbnailUrl, productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }


    //상품 삭제
    @DeleteMapping("owner/{storeId}/{productCode}")
    public ResponseEntity<String> productDelete (
            @PathVariable("storeId") int storeId,
            @RequestHeader("X-User-Id") int userId,
            @PathVariable("productCode") int productCode
    ) {
        productService.productDelete(storeId, userId, productCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("success");
    }






}
