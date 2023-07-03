package com.example.paymentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer price;
}
