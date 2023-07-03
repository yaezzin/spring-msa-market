package com.example.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequest {
    private String productName;
    private Integer unitPrice;
    private Integer stock;
}
