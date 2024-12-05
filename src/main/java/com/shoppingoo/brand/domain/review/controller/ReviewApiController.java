package com.shoppingoo.brand.domain.review.controller;

import com.shoppingoo.brand.domain.filestorage.service.FileStorageService;
import com.shoppingoo.brand.domain.review.dto.ReviewRequest;
import com.shoppingoo.brand.domain.review.dto.ReviewResponse;
import com.shoppingoo.brand.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.codec.multipart.Part;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public Mono<ResponseEntity<ReviewResponse>> reviewRegister(
            @PathVariable("productCode") int productCode,
            @RequestHeader("userId") int userId,
            @RequestPart("reviewRequest") @Valid ReviewRequest reviewRequest,
            @RequestPart(value = "imageUrl", required = false) List<Part> imageUrl
    ) {
        // 비동기로 imageUrl 파일 저장
        Mono<List<String>> imageUrlFileNamesMono = Mono.justOrEmpty(imageUrl)
                .flatMapMany(Flux::fromIterable)
                .flatMap(file -> fileStorageService.saveImageFile(file)
                        .subscribeOn(Schedulers.boundedElastic())) // 블로킹 작업 별도 스레드
                .collectList();

        // 이미지 파일 이름을 처리한 후, 리뷰 등록
        return imageUrlFileNamesMono
                .flatMap(files -> Mono.defer(() ->
                        reviewService.reviewRegister(productCode, userId, reviewRequest, files)
                                .subscribeOn(Schedulers.boundedElastic()) // 블로킹 작업 별도 스레드
                ))
                .map(reviewResponse -> ResponseEntity.status(HttpStatus.CREATED).body(reviewResponse));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewCode}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("reviewCode") int reviewCode) {
        reviewService.reviewDelete(reviewCode);
        return ResponseEntity.noContent().build(); // 204 No Content 반환
    }
}