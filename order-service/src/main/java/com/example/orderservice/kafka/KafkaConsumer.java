package com.example.orderservice.kafka;

import com.example.orderservice.service.OrderService;
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

    private final OrderService orderService;

    @KafkaListener(topics = "order_rollback")
    public void processMessage(String kafkaMessage) {

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Long orderId = Long.parseLong(map.get("orderId").toString());
        log.info("from Inventory service to Order service");

        orderService.rollbackOrder(orderId);
    }
}
