package com.shoppingoo.brand.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "store")
@Getter
@Setter
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT를 사용
    private int id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "logo_url", length = 300)
    private String logoUrl;

}