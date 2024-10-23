package com.shoppingoo.brand.domain.service;

import com.shoppingoo.brand.adapter.jpa.StoreRepository;
import com.shoppingoo.brand.domain.model.StoreEntity;
import com.shoppingoo.brand.domain.model.StoreRegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;


    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository; // 초기화
    }

    public StoreEntity registerStore(StoreRegisterRequest storeRegisterRequest) {
        StoreEntity store = new StoreEntity();
        store.setName(storeRegisterRequest.getName());
        store.setLogoUrl(storeRegisterRequest.getLogoUrl());
        return storeRepository.save(store);
    }
}
