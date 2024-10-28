package com.shoppingoo.brand.domain.store.service;

import com.shoppingoo.brand.db.store.Store;
import com.shoppingoo.brand.domain.store.dto.StoreRequest;
import com.shoppingoo.brand.domain.store.dto.StoreResponse;

public interface StoreService {

    StoreResponse storeRegister(int userId, StoreRequest storeRequest);
}
