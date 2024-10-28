package com.shoppingoo.brand.domain.store.service;

import com.shoppingoo.brand.domain.store.dto.StoreRequest;
import com.shoppingoo.brand.domain.store.dto.StoreResponse;

import java.util.List;

public interface StoreService {

    StoreResponse storeRegister(int userId, StoreRequest storeRequest);
    List<StoreResponse> getAllStores();

}
