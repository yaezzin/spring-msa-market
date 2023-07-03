package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public void payment(PaymentRequest request) {
        Payment payment = Payment.builder()
                .memberId(request.getMemberId())
                .productId(request.getProductId())
                .price(request.getPrice())
                .build();

        // 실제 결제 로직은 수행하지 않고 저장만!
        paymentRepository.save(payment);
    }
}
