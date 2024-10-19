package com.rafflease.payments_service.Payments.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private String stripePaymentId;
}