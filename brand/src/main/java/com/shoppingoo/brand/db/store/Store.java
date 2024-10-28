package com.shoppingoo.brand.db.store;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Entity
@Table(name = "store")
@Getter
@Setter
@Component
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT를 사용
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "license_number", nullable = false, length = 50)
    private int licenseNumber;

    @Column(name = "logo_url", length = 300)
    private String logoUrl;

}
