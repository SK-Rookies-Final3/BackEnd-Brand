package com.shoppingoo.brand.domain.store.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StoreResponse {

    private int id;

    private int userId;

    private String name;

    private int licenseNumber;

    private String logoUrl;
}
