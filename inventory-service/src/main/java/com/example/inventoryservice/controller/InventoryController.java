package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class InventoryController {

    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory")
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        List<InventoryResponse> response = inventoryService.getAllInventory();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/inventory")
    public ResponseEntity<InventoryResponse> create(@RequestBody InventoryRequest request) {
        InventoryResponse response = inventoryService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/inventory/{id}")
    public ResponseEntity<InventoryResponse> update(@PathVariable Long id, @RequestBody InventoryRequest request) {
        InventoryResponse response = inventoryService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/inventory/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
