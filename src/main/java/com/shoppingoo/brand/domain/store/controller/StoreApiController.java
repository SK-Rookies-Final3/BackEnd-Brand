package com.shoppingoo.brand.domain.store.controller;

import com.shoppingoo.brand.domain.store.dto.StatusRequest;
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

    // 가게 등록 요청(role == owner)
    @PostMapping("/owner/register")
    public ResponseEntity<StoreResponse> storeRegister(
            @RequestBody StoreRequest storeRequest,
            @RequestHeader("X-User-Id") int userId
    ) {
        StoreResponse storeResponse = storeService.storeRegister(userId, storeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponse);
    }


    // 가게 권한 수정(role == master)
    @PutMapping("/master/{storeId}/status")
    public ResponseEntity<StoreResponse> updateStoreStatus(
            @RequestBody StatusRequest statusRequest,
            @RequestHeader("X-User-Id") int userId
            ) {
        StoreResponse storeResponse = storeService.updateStoreStatus(userId, statusRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponse);
    }
}

