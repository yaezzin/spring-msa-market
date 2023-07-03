package com.example.orderservice.kafka;

import com.example.orderservice.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Order send(String kafkaTopic, Order order) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(kafkaTopic, jsonInString);
        log.info("kafka producer send data from the order microservice : " + order);
        return order;
    }
}
