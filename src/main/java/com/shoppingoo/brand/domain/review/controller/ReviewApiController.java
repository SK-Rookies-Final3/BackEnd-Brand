package com.shoppingoo.brand.domain.review.controller;

import com.shoppingoo.brand.domain.filestorage.service.FileStorageService;
import com.shoppingoo.brand.domain.product.dto.ProductRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import com.shoppingoo.brand.domain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("/api/brand/review")
public class ReviewApiController {

    private final ReviewService reviewService;
    private final FileStorageService fileStorageService;

    // 생성자 명시
    public ReviewApiController(ReviewService reviewService, FileStorageService fileStorageService) {
        this.reviewService = reviewService;
        this.fileStorageService = fileStorageService;
    }

    // 리뷰 등록
    @PostMapping(value = "/{productCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<ReviewResponse>> createReview(
            @PathVariable("productCode") int productCode,
            @RequestParam("userId") int userId,
            @RequestPart("reviewRequest") ReviewRequest reviewRequest,
            @RequestPart(value = "imageUrl", required = false) List<Part> imageUrl) {

        // 비동기로 image 파일 저장
        Mono<List<String>> imageFileNamesMono = Mono.justOrEmpty(imageUrl)
                .flatMapMany(Flux::fromIterable)
                .flatMap(file -> fileStorageService.saveImageFile(file)
                        .subscribeOn(Schedulers.boundedElastic())) // 블로킹 작업 별도 스레드
                .collectList();

        // 이미지 파일 이름을 처리한 후, 리뷰 등록
        return imageFileNamesMono.flatMap(files -> Mono.defer(() ->
                        reviewService.createReview(productCode, userId, reviewRequest, files)
                                .subscribeOn(Schedulers.boundedElastic()) // 블로킹 작업 별도 스레드
                ))
                .map(reviewResponse -> ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("reviewId") int reviewId,
            @RequestParam("userId") int userId) { // userId를 추가로 받음
        reviewService.reviewDelete(reviewId, userId); // userId를 전달
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }

}