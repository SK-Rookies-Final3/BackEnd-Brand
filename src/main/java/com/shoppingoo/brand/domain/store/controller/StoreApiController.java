package com.shoppingoo.brand.domain.store.controller;

import com.shoppingoo.brand.db.store.Store;
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

    // TODO user API 사용,
    // TODO 관리자 인증 후 가게 등록 로직 추가
    @PostMapping("/owner/register/{userId}")
    public ResponseEntity<StoreResponse> storeRegister(
            @PathVariable("userId") int userId,
            @RequestBody StoreRequest storeRequest
    ) {
        StoreResponse storeResponse = storeService.storeRegister(userId, storeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponse);
    }


}
