package com.shoppingoo.brand.adapter.web;


import com.shoppingoo.brand.domain.model.StoreEntity;
import com.shoppingoo.brand.domain.model.StoreRegisterRequest;
import com.shoppingoo.brand.domain.model.StoreResponse;
import com.shoppingoo.brand.domain.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/brand")
@CrossOrigin(origins="http://localhost:3000")
public class BrandApiController {

    private final StoreService storeService;


    @PostMapping("/store/owner/register")
    public ResponseEntity<StoreResponse> registerStore(@RequestBody StoreRegisterRequest storeRegisterRequest) {
        StoreEntity newStore = storeService.registerStore(storeRegisterRequest);
        StoreResponse storeResponse = new StoreResponse(newStore.getId(), newStore.getName(), newStore.getLogoUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(storeResponse);
    }


}
