package com.example.inventoryservice.kafka;

import com.example.inventoryservice.dto.PaymentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String kafkaTopic, PaymentRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("kafka producer send (inventory service -> payment service) : ");
        kafkaTemplate.send(kafkaTopic, jsonInString);
    }

    public void rollback(String kafkaTopic, Long orderId) {
        Map<String, Object> message = new HashMap<>();
        message.put("orderId", orderId);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("kafka producer rollback (inventory service -> order service)");
        kafkaTemplate.send(kafkaTopic, jsonInString);
    }
}
