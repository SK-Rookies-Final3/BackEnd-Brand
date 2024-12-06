package com.shoppingoo.brand.db.shorts;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortsRepository extends JpaRepository<Shorts, Integer> {

    List<Shorts> findByProductCode(int productCode);
}
