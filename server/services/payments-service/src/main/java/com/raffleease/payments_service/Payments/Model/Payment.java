package com.raffleease.payments_service.Payments.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Payment {
    @Id
    private String id;
    private Long orderId;
    private String paymentMethod;
    private BigDecimal total;
    private String stripePaymentId;
}