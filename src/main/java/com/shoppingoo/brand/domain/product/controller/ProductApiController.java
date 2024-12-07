package com.shoppingoo.brand.domain.product.controller;

import com.shoppingoo.brand.domain.filestorage.service.FileStorageService;
import com.shoppingoo.brand.domain.product.dto.ProductRequest;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
//import com.shoppingoo.brand.domain.product.dto.ShortsResponse;
import com.shoppingoo.brand.domain.product.dto.StockRequest;
import com.shoppingoo.brand.domain.product.service.ProductService;
import com.shoppingoo.brand.domain.store.dto.StatusRequest;
import com.shoppingoo.brand.domain.store.dto.StoreRequest;
import com.shoppingoo.brand.domain.store.dto.StoreResponse;
import io.netty.util.Timeout;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/brand/product")
public class ProductApiController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;

    // 숏츠
    @Value("${flask.api.url}")
    private String flaskApiUrl;

    // 상품 등록
    @PostMapping(value = "/owner/{storeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<ProductResponse>> productRegister(
            @PathVariable("storeId") int storeId,
            @RequestHeader("X-User-Id") int userId,
            @RequestPart("productRequest") ProductRequest productRequest,
            @RequestPart(value = "thumbnail", required = false) List<Part> thumbnail,
            @RequestPart(value = "images", required = false) List<Part> images
    ) {
        // 비동기로 thumbnail 파일 저장
        Mono<List<String>> thumbnailFileNamesMono = Mono.justOrEmpty(thumbnail)
                .flatMapMany(Flux::fromIterable)
                .flatMap(file -> fileStorageService.saveImageFile(file)
                        .subscribeOn(Schedulers.boundedElastic())) // 블로킹 작업 별도 스레드
                .collectList();

        // 비동기로 image 파일 저장
        Mono<List<String>> imageFileNamesMono = Mono.justOrEmpty(images)
                .flatMapMany(Flux::fromIterable)
                .flatMap(file -> fileStorageService.saveImageFile(file)
                        .subscribeOn(Schedulers.boundedElastic())) // 블로킹 작업 별도 스레드
                .collectList();


        // thumbnail, images 처리 후 product 등록
        return Mono.zip(thumbnailFileNamesMono, imageFileNamesMono)
                .flatMap(files -> Mono.defer(() ->
                        productService.productRegister(storeId, userId, productRequest, files.getT1(), files.getT2())
                                .subscribeOn(Schedulers.boundedElastic()) // 블로킹 작업 별도 스레드
                ))
                .doOnNext(this::triggerFlaskApp) // product 등록 후 Flask 호출
                .map(productResponse -> ResponseEntity.status(HttpStatus.CREATED).body(productResponse));
    }


    private void triggerFlaskApp(ProductResponse productResponse) {

        int connectTimeout = 5000; // 연결 타임아웃 (5초)
        int readTimeout = 600000; // 읽기 타임아웃 (10분)

        // HttpClient 설정
//        RequestConfig config = RequestConfig.custom()
//                .setConnectTimeout(connectTimeout)
//                .setSocketTimeout(readTimeout)
//                .build();
//
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setDefaultRequestConfig(config)
//                .build();

        // HttpComponentsClientHttpRequestFactory에 HttpClient 설정
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setConnectionRequestTimeout(readTimeout);
        RestTemplate restTemplate = new RestTemplate(factory);
        String url = flaskApiUrl;

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

    // 상품 수정
    @PatchMapping(value = "/owner/{storeId}/{productCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<ProductResponse>> productUpdate(
            @PathVariable("storeId") int storeId,
            @RequestHeader("X-User-Id") int userId,
            @PathVariable("productCode") int productCode,
            @RequestPart("productRequest") ProductRequest productRequest,
            @RequestPart(value = "thumbnail", required = false) List<Part> thumbnail,
            @RequestPart(value = "images", required = false) List<Part> images
    ) {

        // 비동기로 thumbnail 파일 저장
        Mono<List<String>> thumbnailFileNamesMono = Mono.justOrEmpty(thumbnail)
                .flatMapMany(Flux::fromIterable)
                .flatMap(file -> fileStorageService.saveImageFile(file)
                        .subscribeOn(Schedulers.boundedElastic())) // 블로킹 작업 별도 스레드
                .collectList();

        // 비동기로 image 파일 저장
        Mono<List<String>> imageFileNamesMono = Mono.justOrEmpty(images)
                .flatMapMany(Flux::fromIterable)
                .flatMap(file -> fileStorageService.saveImageFile(file)
                        .subscribeOn(Schedulers.boundedElastic())) // 블로킹 작업 별도 스레드
                .collectList();

        // thumbnail, images 처리 후 product 수정
        return Mono.zip(thumbnailFileNamesMono, imageFileNamesMono)
                .flatMap(files -> Mono.defer(() ->
                        productService.productUpdate(storeId, userId, productCode, productRequest, files.getT1(), files.getT2())
                                .subscribeOn(Schedulers.boundedElastic()) // 블로킹 작업 별도 스레드
                ))
                .map(productResponse -> ResponseEntity.status(HttpStatus.OK).body(productResponse));
    }



    //상품 삭제
    @DeleteMapping("owner/{storeId}/{productCode}")
    public ResponseEntity<String> productDelete (
            @PathVariable("storeId") int storeId,
            @RequestHeader("X-User-Id") int userId,
            @PathVariable("productCode") int productCode
    ) {
        productService.productDelete(storeId, userId, productCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("success");
    }

    // 사용자(owner)의 본인 가게의 상품 전체 조회
    @GetMapping("/owner")
    public ResponseEntity<List<ProductResponse>> getProductByUserId(
            @RequestHeader("X-User-Id") int userId) {
        List<ProductResponse> productResponses = productService.getProductByUserId(userId);

        if (!productResponses.isEmpty()) {
            return ResponseEntity.ok(productResponses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 사용자(owner)의 본인 가게의 상품 전체 조회 -- order 사용
    @GetMapping("/ownerRest")
    public ResponseEntity<List<ProductResponse>> getProductByUserIds(
            @RequestHeader("X-User-Id") int userId) {
        System.out.println("ownerRest userId:::"+userId);
        List<ProductResponse> productResponses = productService.getProductByUserIds(userId);

        if (!productResponses.isEmpty()) {
            return ResponseEntity.ok(productResponses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
