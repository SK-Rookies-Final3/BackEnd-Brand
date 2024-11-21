package com.shoppingoo.brand.domain.review.controller;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import com.shoppingoo.brand.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/brand/review")
public class ReviewOpenApiController {

    private final ReviewService reviewService;

    @GetMapping("/{productCode}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(@PathVariable("productCode") int productCode) {

        List<ReviewResponse> reviews = reviewService.getReviewsByProductCode(productCode);
        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build(); // 리뷰가 없는 경우 204 반환
        }
        return ResponseEntity.ok(reviews); // 리뷰 리스트 반환
    }
}