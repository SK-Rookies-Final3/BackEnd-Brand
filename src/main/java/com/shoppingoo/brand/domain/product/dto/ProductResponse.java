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
public class ProductResponse {

    private int code;

    private int storeId;

    private String name;

    private int price;

    private List<String> thumbnail;

    private Category category;

    private int stock;

    private LocalDateTime registerAt;

}
