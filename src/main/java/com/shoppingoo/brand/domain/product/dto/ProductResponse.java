package com.shoppingoo.brand.domain.product.dto;

import com.shoppingoo.brand.db.product.enums.Category;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductResponse {

    private int userId;

    private int code;

    private int storeId;

    private String name;

    private int price;

    private int stock;

    // 썸네일을 여러 개 받을 수 있도록 List<String>으로 변경
    private List<String> thumbnailFileNames;

    private String textInformation;

    // 이미지들을 여러 개 받을 수 있도록 List<String>으로 변경
    private List<String> imageFileNames;

    private Category category;

    private String color;

    private String clothesSize;

    private String shoesSize;
}
