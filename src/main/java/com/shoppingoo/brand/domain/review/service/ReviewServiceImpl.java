package com.shoppingoo.brand.domain.review.service;
import com.shoppingoo.brand.db.product.Product;
import com.shoppingoo.brand.db.product.ProductRepository;
import com.shoppingoo.brand.db.review.Review;
import com.shoppingoo.brand.db.review.ReviewRepository;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository, ModelMapper modelMapper, RestTemplate restTemplate) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    // **더미 로직: username 조회**
    private String getUsernameByUserId(int userId) {
        // 실제 구현 대신 간단한 더미 값 반환
        return "dummy_username"; // 사용자 이름을 하드코딩으로 반환
    }

    // **더미 로직: order_item_id를 통해 product_code 조회**
    private int getProductCodeByOrderItemId(int orderItemId) {
        // 실제 구현 대신 간단한 더미 값 반환
        return 101; // 고정된 product_code 반환
    }

    // **1. 리뷰 등록**
    @Override
    public ReviewResponse reviewRegister(int productCode, int userId, int orderItemId, ReviewRequest reviewRequest) {
        // orderItemId로 productCode 확인
        int orderItemProductCode = getProductCodeByOrderItemId(orderItemId);

        if (orderItemProductCode != productCode) {
            throw new RuntimeException("Mismatch between provided product_code and order_item's product_code");
        }

        // 기존 로직 유지
        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found with code: " + productCode));

        String username = getUsernameByUserId(userId);

        Review review = modelMapper.map(reviewRequest, Review.class);
        review.setProductCode(productCode);
        review.setUserId(userId);
        review.setUsername(username);
        review.setReviewDate(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        return modelMapper.map(savedReview, ReviewResponse.class);
    }


    // **2. 특정 상품의 리뷰 조회**
    @Override
    public List<ReviewResponse> getReviewsByProductCode(int productCode) {
        List<Review> reviews = reviewRepository.findByProductCode(productCode);

        if (reviews.isEmpty()) {
            throw new RuntimeException("No reviews found for product code: " + productCode);
        }

        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse getReviewById(int reviewId) {
        return null;
    }

    @Override
    public ReviewResponse reviewRegister(int productCode, int userId, String imageUrl, ReviewRequest reviewRequest) {
        return null;
    }

    // **3. 리뷰 삭제**
    @Override
    public void reviewDelete(int productCode, int userId, int reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        if (review.getProductCode() != productCode) {
            throw new RuntimeException("Product code mismatch for review: " + reviewId);
        }

        if (review.getUserId() != userId) {
            throw new RuntimeException("User does not have permission to delete this review");
        }

        reviewRepository.delete(review);
    }
}