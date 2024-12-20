package com.shoppingoo.brand.domain.review.service;
import com.shoppingoo.brand.db.product.Product;
import com.shoppingoo.brand.db.product.ProductRepository;
import com.shoppingoo.brand.db.review.Review;
import com.shoppingoo.brand.db.review.ReviewRepository;
import com.shoppingoo.brand.domain.filestorage.service.FileStorageService;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final FileStorageService fileStorageService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository, RestTemplate restTemplate, ModelMapper modelMapper, FileStorageService fileStorageService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Mono<ReviewResponse> reviewRegister(int productCode, int userId, ReviewRequest reviewRequest,
                                               List<String> imageUrlFileNames) {
        try {
            // reivew 객체 생성 및 설정
            Review review = modelMapper.map(reviewRequest, Review.class);

            // 유저 아이디 설정
            review.setUserId(userId);
            review.setReviewDate(LocalDateTime.now());

            // 썸네일과 이미지 파일 이름을 List<String> 형태로 설정
            review.setImageUrl(imageUrlFileNames); // 여러 이미지 파일 이름들을 List로 설정

            // Review 저장
            reviewRepository.save(review);

            // 모델 매핑 후 Mono로 감싸서 리턴
            return Mono.just(modelMapper.map(review, ReviewResponse.class));  // Mono로 감싸서 반환
        } catch (Exception e) {
            throw new RuntimeException("Review registration failed", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductCode(int productCode) {
        try {
            // 리뷰 데이터 검색
            List<Review> reviews = reviewRepository.findByProductCode(productCode);

            // 리뷰가 없을 경우 예외 발생
            if (reviews == null || reviews.isEmpty()) {
                System.err.println("No reviews found for productCode: " + productCode);
                throw new RuntimeException("No reviews found for productCode: " + productCode);
            }

            // Review -> ReviewResponse 변환
            return reviews.stream()
                    .map(review -> {
                        ReviewResponse reviewResponse = modelMapper.map(review, ReviewResponse.class);

                        // 이미지 경로 설정
                        List<String> imageUrls = review.getImageUrl() != null
                                ? review.getImageUrl()
                                : Collections.emptyList();
                        reviewResponse.setImageUrl(imageUrls);

                        return reviewResponse;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // 에러를 로깅하고 상세 메시지 반환
            System.err.println("Error fetching reviews for productCode: " + productCode);
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch reviews: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void reviewDelete(int reviewCode) {
        Review review = reviewRepository.findById(reviewCode)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewCode));

        reviewRepository.delete(review);
    }
}
