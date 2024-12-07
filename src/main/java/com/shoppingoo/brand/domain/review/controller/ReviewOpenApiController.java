package com.shoppingoo.brand.domain.review.controller;
import com.shoppingoo.brand.domain.product.dto.ProductAllResponse;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import com.shoppingoo.brand.domain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/open-api/brand/review")
public class ReviewOpenApiController {

    private final ReviewService reviewService;

    // 생성자 명시
    public ReviewOpenApiController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{productCode}")
    public ResponseEntity<?> getReviewsByProduct(@PathVariable("productCode") int productCode) {
        try {
            List<ReviewResponse> reviews = reviewService.getReviewsByProductCode(productCode);
            return ResponseEntity.ok(reviews);
        } catch (RuntimeException ex) {
            // 예외 메시지와 함께 400 또는 500 상태 코드 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
