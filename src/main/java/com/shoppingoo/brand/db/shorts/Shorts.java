package com.shoppingoo.brand.db.shorts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shorts")
public class Shorts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "product_code", nullable = false)
    private int productCode;

    @Column(name = "shorts_id")
    private String shortsId;

    @Column(name = "short_url")
    private String shortsUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "sentiment_label")
    private String sentimentLabel;

    @Column(name = "sentiment_score")
    private Float sentimentScore;



}
