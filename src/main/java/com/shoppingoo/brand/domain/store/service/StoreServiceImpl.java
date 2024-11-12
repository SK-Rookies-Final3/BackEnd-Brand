package com.shoppingoo.brand.domain.store.service;

import com.shoppingoo.brand.db.product.Product;
import com.shoppingoo.brand.db.store.Store;
import com.shoppingoo.brand.db.store.StoreRepository;
import com.shoppingoo.brand.domain.product.dto.ProductResponse;
import com.shoppingoo.brand.domain.store.dto.StoreRequest;
import com.shoppingoo.brand.domain.store.dto.StoreResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository, ModelMapper modelMapper) {
        this.storeRepository = storeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StoreResponse storeRegister(int userId, StoreRequest storeRequest) {
        // StoreRequest를 Store 엔티티로 매핑
        Store store = modelMapper.map(storeRequest, Store.class);

        // 추가 필드 설정 (userId)
        store.setUserId(userId);

        // Store 저장
        Store savedStore = storeRepository.save(store);

        // 저장된 Store 엔티티를 StoreResponse로 변환하여 반환
        return modelMapper.map(savedStore, StoreResponse.class);
    }


    @Override
    public List<StoreResponse> getAllStores() {
        return storeRepository.findAll().stream()
                .map(store -> new StoreResponse(
                        store.getId(),
                        store.getUserId(),
                        //store.getUsername(),
                        store.getName(),
                        store.getLicenseNumber(),
                        store.getStatus(),
                        store.getRegisteredAt()))
                .collect(Collectors.toList());
    }
}
