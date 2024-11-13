package com.shoppingoo.brand.domain.review.service;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ReviewServiceImpl implements com.shoppingoo.brand.domain.review.service.ReviewService {

    private final com.shoppingoo.brand.db.review.ReviewRepository reviewRepository;

    @Autowired
    public ReviewServiceImpl(com.shoppingoo.brand.db.review.ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ReviewResponse> getReviewsByProductCode(Integer productCode) {
        return reviewRepository.findByProductCode(productCode)
                .stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        review.getOrderItemId(),
                        review.getProductCode(),
                        review.getUserNickname(),
                        review.getHeight(),
                        review.getWeight(),
                        review.getOption(),
                        review.getContent(),
                        review.getSize(),
                        review.getStarRating(),
                        review.getColor(),
                        review.getImageUrl(),
                        review.getReviewDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse addReview(ReviewRequest request) {
        com.shoppingoo.brand.db.review.Review review = new com.shoppingoo.brand.db.review.Review(
                null,
                request.getOrderItemId(),
                request.getProductCode(),
                request.getUserNickname(),
                request.getHeight(),
                request.getWeight(),
                request.getOption(),
                request.getContent(),
                request.getSize(),
                request.getStarRating(),
                request.getColor(),
                request.getImageUrl(),
                LocalDateTime.now()
        );
        com.shoppingoo.brand.db.review.Review savedReview = reviewRepository.save(review);
        return new ReviewResponse(
                savedReview.getId(),
                savedReview.getOrderItemId(),
                savedReview.getProductCode(),
                savedReview.getUserNickname(),
                savedReview.getHeight(),
                savedReview.getWeight(),
                savedReview.getOption(),
                savedReview.getContent(),
                savedReview.getSize(),
                savedReview.getStarRating(),
                savedReview.getColor(),
                savedReview.getImageUrl(),
                savedReview.getReviewDate()
        );
    }

    @Override
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}