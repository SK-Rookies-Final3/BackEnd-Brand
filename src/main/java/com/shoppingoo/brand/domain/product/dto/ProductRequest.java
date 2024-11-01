package com.shoppingoo.brand.domain.product.dto;

import com.shoppingoo.brand.db.product.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private String name;

    private int price;

    private int stock;

    private String thumbnailUrl;

    private String textInformation;

    private String imageInformation;

    private Category category;

    private String color;

    private String clothesSize;

    private String shoesSize;

}
