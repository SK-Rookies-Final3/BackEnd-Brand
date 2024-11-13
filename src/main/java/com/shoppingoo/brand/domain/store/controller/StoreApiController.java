package com.shoppingoo.brand.domain.store.controller;

import com.shoppingoo.brand.domain.store.dto.StoreRequest;
import com.shoppingoo.brand.domain.store.dto.StoreResponse;
import com.shoppingoo.brand.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/store")
@CrossOrigin(origins = "http://localhost:3000")
public class StoreApiController {

    private final StoreService storeService;

    // 가게 등록 요청
    @PostMapping("/owner/register")
    public ResponseEntity<StoreResponse> storeRegister(
            @RequestBody StoreRequest storeRequest,
            @RequestHeader("X-User-Id") int userId
    ) {
        StoreResponse storeResponse = storeService.storeRegister(userId, storeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponse);
    }

    // 게이트웨이 테스트용 REST API
    @GetMapping("/some-endpoint")
    public String getUserId(@RequestHeader("X-User-Id") int userId) {

        // X-User-Id 헤더 값 사용(포스트맨 테스트 시에는 임의로 키와 벨류를 넣어줘야 합니다)
        return "User ID: " + userId;
    }
}
