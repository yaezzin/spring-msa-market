package com.example.inventoryservice.service;


import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> getAllInventory();
    InventoryResponse create(InventoryRequest request);
    InventoryResponse update(Long id, InventoryRequest request);
    void delete(Long id);
    boolean isStockAvailable(Long productId, Integer quantity);
    void decreaseStock(Long productId, Integer quantity);
    void payment(Long memberId, Long productId, Integer quantity);
    void rollbackOrder(Long productId);
}
