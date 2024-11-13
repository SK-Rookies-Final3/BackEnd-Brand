package com.shoppingoo.brand.domain.review.service;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import java.util.List;

public interface ReviewService {
    List<ReviewResponse> getReviewsByProductCode(Integer productCode);
    ReviewResponse addReview(ReviewRequest request);
    void deleteReview(Integer reviewId);
}