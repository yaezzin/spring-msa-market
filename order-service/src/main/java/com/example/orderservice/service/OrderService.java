package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.Order;

public interface OrderService {
    Order createOrder(Long memberId, OrderRequest request);
    void rollbackOrder(Long orderId);
}
