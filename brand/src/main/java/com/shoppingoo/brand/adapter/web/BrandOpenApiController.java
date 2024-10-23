package com.shoppingoo.brand.adapter.web;


import com.shoppingoo.brand.domain.model.StoreRegisterRequest;
import com.shoppingoo.brand.domain.model.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/brand")
@CrossOrigin(origins="http://localhost:3000")
public class BrandOpenApiController {

    @GetMapping("brand/store/{userId}")
    public ResponseEntity<StoreResponse> registerStore(
            @RequestBody StoreRegisterRequest storeRegisterRequest) {

    }


}
