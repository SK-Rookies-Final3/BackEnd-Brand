package com.shoppingoo.brand.domain.store.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/brand")
@CrossOrigin(origins="http://localhost:3000")
public class StoreOpenApiController {

//    private final StoreService storeService;
//
//    @GetMapping("brand/store/{userId}")
//    public ResponseEntity<StoreResponse> StoreInfo(
//            @PathVariable Integer userId) {
//
//        StoreResponse storeResponse = storeService.getStoreInfo(userId); // 서비스 호출
//        return ResponseEntity.ok(storeResponse); // 응답 반환
//
//    }


}
