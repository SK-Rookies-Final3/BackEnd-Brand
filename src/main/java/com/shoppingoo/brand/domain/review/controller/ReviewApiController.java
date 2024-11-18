package com.shoppingoo.brand.domain.review.controller;

import com.shoppingoo.brand.db.product.ProductRepository;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import com.shoppingoo.brand.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/review")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewApiController {

    private final ReviewService reviewService;
    private final ProductRepository productRepository;

    // Review 등록
    @PostMapping("/{userId}/{productCode}/{reviewId}")
    public ResponseEntity<ReviewResponse> reviewRegister(
            @PathVariable("productCode") int productCode,
            @RequestHeader(value = "X-User-Id", defaultValue = "0") int userId,
            @RequestParam("image_url") String imageUrl,
            @RequestBody ReviewRequest reviewRequest
    ) {
        ReviewResponse reviewResponse = reviewService.reviewRegister(productCode, userId, imageUrl, reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse);
    }

    // Review 삭제
    @DeleteMapping("/exit/{reviewId}")
    public ResponseEntity<String> reviewDelete(
            @PathVariable("productCode") int productCode,
            @RequestHeader("X-User-Id") int userId,
            @PathVariable("reviewId") int reviewId
    ) {
        reviewService.reviewDelete(productCode, userId, reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("success");
    }
}
