package com.example.inventoryservice.kafka;

import com.example.inventoryservice.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "order_create")
    public void processMessage(String kafkaMessage) {

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long orderId = Long.parseLong(map.get("id").toString());
        Long productId = Long.parseLong(map.get("productId").toString());
        Long memberId = Long.parseLong(map.get("memberId").toString());
        Integer quantity = (Integer) map.get("quantity");

        log.info("from Order service to Inventory service");

        if (inventoryService.isStockAvailable(productId, quantity)) {
            inventoryService.decreaseStock(productId, quantity);
            inventoryService.payment(memberId, productId, quantity);
        } else {
            inventoryService.rollbackOrder(orderId);
        }
    }
}
