package com.shoppingoo.brand.domain.review.controller;

import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import com.shoppingoo.brand.domain.review.service.ReviewService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open-api/review")
public class ReviewOpenApiController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewOpenApiController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{productCode}")
    public List<ReviewResponse> getPublicReviews(@PathVariable Integer productCode) {
        return reviewService.getReviewsByProductCode(productCode);
    }
}