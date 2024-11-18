package com.shoppingoo.brand.domain.review.service;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse reviewRegister(int productCode, int userId, int orderItemId, ReviewRequest reviewRequest);
    List<ReviewResponse> getReviewsByProductCode(int productCode);
    ReviewResponse getReviewById(int reviewId);

    // API Gateway를 고려한 추가 메서드
    ReviewResponse reviewRegister(int productCode, int userId, String imageUrl, ReviewRequest reviewRequest);
    void reviewDelete(int productCode, int userId, int reviewId);
}