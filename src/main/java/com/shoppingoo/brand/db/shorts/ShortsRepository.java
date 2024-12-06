package com.shoppingoo.brand.db.shorts;

import com.shoppingoo.brand.db.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShortsRepository extends JpaRepository<Shorts, Integer> {

    List<Shorts> findByProductCode(int productCode);
}
