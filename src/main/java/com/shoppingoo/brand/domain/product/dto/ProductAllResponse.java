package com.shoppingoo.brand.domain.product.dto;

import com.shoppingoo.brand.db.product.enums.Category;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductAllResponse {

    private int code;

    private int storeId;

    private String name;

    private int price;

    private String thumbnail;

    private String images;

    private Category category;

}
