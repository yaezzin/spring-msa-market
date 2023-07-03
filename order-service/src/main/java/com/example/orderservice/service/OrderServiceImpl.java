package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.kafka.KafkaProducer;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;

    @Override
    public Order createOrder(Long memberId, OrderRequest request) {
        Order order = Order.builder()
                .productId(request.getProductId())
                .memberId(memberId)
                .quantity(request.getQuantity())
                .build();

        orderRepository.save(order);
        kafkaProducer.send("order_create", order);
        return order;
    }

    @Override
    public void rollbackOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
