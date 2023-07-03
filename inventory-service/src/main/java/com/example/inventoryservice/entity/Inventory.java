package com.example.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String productName;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Long sellerId;

    @Builder
    public Inventory(String productName, Integer stock, Integer unitPrice, Long sellerId) {
        this.productName = productName;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.sellerId = sellerId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }
}
