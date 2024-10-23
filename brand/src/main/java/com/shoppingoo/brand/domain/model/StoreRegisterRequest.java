package com.shoppingoo.brand.domain.model;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreRegisterRequest {

    @NotNull
    private String name;

    private String logoUrl;

}
