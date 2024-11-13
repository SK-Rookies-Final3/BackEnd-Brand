package com.shoppingoo.brand.domain.review.controller;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import com.shoppingoo.brand.domain.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
public class ReviewApiController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewApiController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ReviewResponse> addReview(@RequestBody ReviewRequest request) {
        ReviewResponse reviewResponse = reviewService.addReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse);
    }

    @DeleteMapping("/exit/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}