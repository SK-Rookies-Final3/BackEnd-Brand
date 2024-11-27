package com.shoppingoo.brand.domain.product.dto;

import com.shoppingoo.brand.db.product.enums.Category;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductAllResponse {

    private int code;
    private int userId;
    private int storeId;
    private String name;
    private int price;
    private int stock;
    private List<String> thumbnail;
    private String textInformation;
    private List<String> images;
    private Category category;
    private String color;
    private String clothesSize;
    private String shoesSize;
    private LocalDateTime registerAt;
}
