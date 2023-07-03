package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentRequest;

public interface PaymentService {
    void payment(PaymentRequest request);
}
