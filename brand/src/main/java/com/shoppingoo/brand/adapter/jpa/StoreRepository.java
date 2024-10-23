package com.shoppingoo.brand.adapter.jpa;

import com.shoppingoo.brand.domain.model.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<StoreEntity, Integer> {
}
