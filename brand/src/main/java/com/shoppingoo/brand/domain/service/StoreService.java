package com.shoppingoo.brand.domain.service;

import com.shoppingoo.brand.domain.model.StoreEntity;
import com.shoppingoo.brand.domain.model.StoreRegisterRequest;

public interface StoreService {

    StoreEntity registerStore(StoreRegisterRequest storeRegisterRequest); // 가게 등록 메서드

}
