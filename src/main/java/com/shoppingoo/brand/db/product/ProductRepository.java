package com.shoppingoo.brand.db.product;

import com.shoppingoo.brand.db.product.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategory(Category category);
    List<Product> findByStoreId(int storeId);
    List<Product> findByUserId(int userId);

}