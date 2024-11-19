package com.shoppingoo.brand.domain.review.controller;

import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import com.shoppingoo.brand.domain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewApiController {

    private final ReviewService reviewService;

    // 생성자 명시
    public ReviewApiController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 등록
    @PostMapping("/{productCode}")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable("productCode") int productCode,
            @RequestParam("userId") int userId,
            @RequestParam("image_url") String imageUrl,
            @RequestBody ReviewRequest reviewRequest) {
        int hardcodedProductCode = 123;
        ReviewResponse reviewResponse = reviewService.reviewRegister(hardcodedProductCode, userId, imageUrl, reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse);
    }

    // 리뷰 조회
    @GetMapping("/{productCode}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(
            @PathVariable("productCode") int productCode) {
        int hardcodedProductCode = 123;
        List<ReviewResponse> reviews = reviewService.getReviewsByProductCode(hardcodedProductCode);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("reviewId") int reviewId) {
        reviewService.reviewDelete(reviewId);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
}
