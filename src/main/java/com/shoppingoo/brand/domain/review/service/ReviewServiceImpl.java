package com.shoppingoo.brand.domain.review.service;
import com.shoppingoo.brand.db.product.ProductRepository;
import com.shoppingoo.brand.db.review.Review;
import com.shoppingoo.brand.db.review.ReviewRepository;
import com.shoppingoo.brand.db.store.StoreRepository;
import com.shoppingoo.brand.domain.filestorage.service.FileStorageService;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper modelMapper;
    private final FileStorageService fileStorageService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository, StoreRepository storeRepository, RestTemplate restTemplate, ModelMapper modelMapper, FileStorageService fileStorageService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
        this.fileStorageService = fileStorageService;
    }

    // 리뷰 등록
    @Override
    public Mono<ReviewResponse> createReview(int productCode, int userId, ReviewRequest reviewRequest,
                                             List<String> imageFileNames) {
        try {
            // Review 객체 생성 및 설정
            Review review = modelMapper.map(reviewRequest, Review.class);

            // 유저 아이디
            review.setProductCode(productCode);
            review.setUserId(userId);
            review.setReviewDate(LocalDateTime.now());

            // S3에 업로드된 이미지 URL 리스트를 review에 설정
            review.setImageUrl(imageFileNames);

            // review 저장
            reviewRepository.save(review);

            // 모델 매핑 후 Mono로 감싸서 리턴
            return Mono.just(modelMapper.map(review, ReviewResponse.class));
        } catch (Exception e) {
            throw new RuntimeException("Review registration failed", e);
        }
    }

    @Override
    public List<ReviewResponse> getReviewsByProductCode(int productCode) {
        List<Review> reviews = reviewRepository.findByProductCode(productCode);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void reviewDelete(int reviewId, int userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        if (review.getUserId() != userId) {
            throw new RuntimeException("You do not have permission to delete this review");
        }

        reviewRepository.delete(review);
    }
}