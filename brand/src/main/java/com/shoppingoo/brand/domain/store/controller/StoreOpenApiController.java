package com.shoppingoo.brand.domain.store.controller;


import com.shoppingoo.brand.domain.store.dto.StoreResponse;
import com.shoppingoo.brand.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/store")
@CrossOrigin(origins="http://localhost:3000")
public class StoreOpenApiController {

    private final StoreService storeService;

    // 전체 조회
    @GetMapping("/")
    public ResponseEntity<List<StoreResponse>> getAllStore() {

        List<StoreResponse> storeResponseList = storeService.getAllStores();
        return ResponseEntity.ok(storeResponseList); // 응답 반환

    }



}
