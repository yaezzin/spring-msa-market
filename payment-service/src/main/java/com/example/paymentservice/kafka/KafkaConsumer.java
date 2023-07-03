package com.example.paymentservice.kafka;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.service.PaymentService;
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

    private final PaymentService paymentService;

    @KafkaListener(topics = "stock_decrease")
    public void processMessage(String kafkaMessage) {

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("# from inventory service to payment service");

        Long productId = Long.parseLong(map.get("productId").toString()); // inventory PK
        Long memberId = Long.parseLong(map.get("memberId").toString());
        Integer price = (Integer) map.get("price");

        PaymentRequest request = new PaymentRequest(productId, memberId, price);
        paymentService.payment(request);
    }
}
