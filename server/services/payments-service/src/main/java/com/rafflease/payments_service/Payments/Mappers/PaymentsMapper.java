package com.rafflease.payments_service.Payments.Mappers;

import com.rafflease.payments_service.Payments.DTO.PaymentDTO;
import com.rafflease.payments_service.Payments.Model.Payment;
import org.springframework.stereotype.Service;

@Service
public class PaymentsMapper {
    public PaymentDTO fromPayment(Payment payment) {
        return PaymentDTO.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .paymentIntentId(payment.getStripePaymentId())
                .build();
    }
}