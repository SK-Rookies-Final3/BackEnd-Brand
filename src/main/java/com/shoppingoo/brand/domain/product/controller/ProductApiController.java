package com.shoppingoo.brand.domain.product.controller;

import com.shoppingoo.brand.s3.S3Service;
import com.shoppingoo.brand.domain.product.dto.ProductAllResponse;
import com.shoppingoo.brand.domain.product.dto.ProductRequest;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
import com.shoppingoo.brand.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/brand/product")
public class ProductApiController {

    private final ProductService productService;
    private final S3Service s3Service;

    @PostMapping(value = "/owner/{storeId}", consumes = "multipart/form-data")
    public Mono<ResponseEntity<ProductAllResponse>> productRegister(
            @PathVariable("storeId") int storeId,
            @RequestHeader("X-User-Id") int userId,
            @RequestPart("productRequest") ProductRequest productRequest,
            @RequestPart(value = "thumbnail", required = false) List<String> thumbnail,
            @RequestPart(value = "images", required = false) List<String> imageInformation) {

        // 상품 등록 서비스 호출 (반환 타입 Mono<ProductAllResponse>)
        Mono<ProductAllResponse> productAllResponseMono = productService.productRegister(storeId, userId, productRequest, thumbnail, imageInformation);

        // Mono의 값을 ResponseEntity로 감싸서 반환
        return productAllResponseMono.map(productAllResponse ->
                new ResponseEntity<>(productAllResponse, HttpStatus.CREATED)
        );
    }



    private void triggerFlaskApp(ProductResponse productResponse) {
        RestTemplate restTemplate = new RestTemplate();
        String flaskApiUrl = "http://localhost:5001/api/shorts/search";

        // Flask API로 보낼 데이터
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("product_code", productResponse.getCode());
        requestData.put("product_name", productResponse.getName());

        // HTTP 요청 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);

        // Flask API 호출
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(flaskApiUrl, requestEntity, String.class);
            System.out.println("Flask API 호출 성공: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Flask API 호출 실패: " + e.getMessage());
        }
    }

//    // 상품 수정
//    @PatchMapping(value = "/owner/{storeId}/{productCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Mono<ResponseEntity<ProductResponse>> productUpdate(
//            @PathVariable("storeId") int storeId,
//            @RequestHeader("X-User-Id") int userId,
//            @PathVariable("productCode") int productCode,
//            @RequestPart("productRequest") ProductRequest productRequest,
//            @RequestPart(value = "thumbnail", required = false) List<Part> thumbnail,
//            @RequestPart(value = "images", required = false) List<Part> images
//    ) {
//
//        // 비동기로 thumbnail 파일 저장
//        Mono<List<String>> thumbnailFileNamesMono = Mono.justOrEmpty(thumbnail)
//                .flatMapMany(Flux::fromIterable)
//                .flatMap(file -> fileStorageService.saveImageFile(file)
//                        .subscribeOn(Schedulers.boundedElastic())) // 블로킹 작업 별도 스레드
//                .collectList();
//
//        // 비동기로 image 파일 저장
//        Mono<List<String>> imageFileNamesMono = Mono.justOrEmpty(images)
//                .flatMapMany(Flux::fromIterable)
//                .flatMap(file -> fileStorageService.saveImageFile(file)
//                        .subscribeOn(Schedulers.boundedElastic())) // 블로킹 작업 별도 스레드
//                .collectList();
//
//        // thumbnail, images 처리 후 product 수정
//        return Mono.zip(thumbnailFileNamesMono, imageFileNamesMono)
//                .flatMap(files -> Mono.defer(() ->
//                        productService.productUpdate(storeId, userId, productCode, productRequest, files.getT1(), files.getT2())
//                                .subscribeOn(Schedulers.boundedElastic()) // 블로킹 작업 별도 스레드
//                ))
//                .map(productResponse -> ResponseEntity.status(HttpStatus.OK).body(productResponse));
//    }
//
//
//
//    //상품 삭제
//    @DeleteMapping("owner/{storeId}/{productCode}")
//    public ResponseEntity<String> productDelete (
//            @PathVariable("storeId") int storeId,
//            @RequestHeader("X-User-Id") int userId,
//            @PathVariable("productCode") int productCode
//    ) {
//        productService.productDelete(storeId, userId, productCode);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("success");
//    }
//
//    // 사용자(owner)의 본인 가게의 상품 전체 조회
//    @GetMapping("/owner")
//    public ResponseEntity<List<ProductResponse>> getProductByUserId(
//            @RequestHeader("X-User-Id") int userId) {
//        List<ProductResponse> productResponses = productService.getProductByUserId(userId);
//
//        if (!productResponses.isEmpty()) {
//            return ResponseEntity.ok(productResponses);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

}
