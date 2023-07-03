package com.example.inventoryservice.service;

import com.example.inventoryservice.client.MemberServiceClient;
import com.example.inventoryservice.dto.PaymentRequest;
import com.example.inventoryservice.kafka.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.entity.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MemberServiceClient memberServiceClient;
    private final KafkaProducer producer;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository, MemberServiceClient memberServiceClient, KafkaProducer producer) {
        this.inventoryRepository = inventoryRepository;
        this.memberServiceClient = memberServiceClient;
        this.producer = producer;
    }

    @Override
    public List<InventoryResponse> getAllInventory() {
        List<Inventory> inventories = inventoryRepository.findAll();
        List<InventoryResponse> response = inventories.stream()
                .map(inventory -> new InventoryResponse(
                        inventory.getId(),
                        inventory.getProductName(),
                        inventory.getUnitPrice(),
                        inventory.getStock(),
                        inventory.getSellerId()
                ))
                .collect(Collectors.toList());
        return response;
    }

    @Override
    public InventoryResponse create(InventoryRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long sellerId = memberServiceClient.findIdByEmail(email);

        Inventory create = Inventory.builder()
                .productName(request.getProductName())
                .stock(request.getStock())
                .unitPrice(request.getUnitPrice())
                .sellerId(sellerId)
                .build();

        Inventory save = inventoryRepository.save(create);

        return new InventoryResponse(save.getId(), save.getProductName(), save.getUnitPrice(), save.getStock(), sellerId);
    }

    @Override
    @Transactional
    public InventoryResponse update(Long id, InventoryRequest request) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
        Long currentUserId = getSellerId();
        if (!inventory.getSellerId().equals(currentUserId)) {
            throw new IllegalArgumentException("Invalid Seller");
        }

        inventory.setProductName(request.getProductName());
        inventory.setUnitPrice(request.getUnitPrice());
        inventory.setStock(request.getStock());
        return new InventoryResponse(inventory.getId(), inventory.getProductName(), inventory.getUnitPrice(), inventory.getStock(), inventory.getSellerId());
    }

    @Override
    public void delete(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
        if (!inventory.getSellerId().equals(getSellerId())) {
            throw new IllegalArgumentException("Invalid Seller");
        }
        inventoryRepository.deleteById(id);
    }

    @Override
    public boolean isStockAvailable(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
        return inventory.getStock() - quantity > 0;
    }

    @Override
    public void decreaseStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
        inventory.setStock(inventory.getStock() - quantity);
        log.info("decrease stock : " + inventory.getStock() + " and quantity" + quantity);
        inventoryRepository.save(inventory);
    }

    @Override
    public void payment(Long memberId, Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
        Integer price = inventory.getUnitPrice() * quantity;
        PaymentRequest request = new PaymentRequest(memberId, inventory.getId(), price);
        producer.send("stock_decrease", request);
    }

    @Override
    public void rollbackOrder(Long orderId) {
        producer.rollback("order_rollback", orderId);
    }

    private Long getSellerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long currentUserId = memberServiceClient.findIdByEmail(email);
        return currentUserId;
    }
}
