package com.shoppingoo.brand.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponse {

    private int id;

    private String name;

    private String logoUrl;
}
