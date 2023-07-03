package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{memberId}/orders")
    public void createOrder(@PathVariable("memberId") Long memberId, @RequestBody OrderRequest request) {
        orderService.createOrder(memberId, request);
    }
}

