package com.shoppingoo.brand.domain.review.service;

import com.shoppingoo.brand.domain.product.dto.ProductRequest;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReviewService {
    Mono<ReviewResponse> createReview(int productCode, int userId, ReviewRequest reviewRequest,
                                      List<String> imageFileNames);

    List<ReviewResponse> getReviewsByProductCode(int productCode);
    void reviewDelete(int reviewId, int userId); // 사용자 ID를 포함하는 수정된 메서드 시그니처
}
