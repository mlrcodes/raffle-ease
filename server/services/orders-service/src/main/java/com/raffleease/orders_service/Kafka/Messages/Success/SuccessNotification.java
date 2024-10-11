package com.raffleease.orders_service.Kafka.Messages.Success;

import com.raffleease.orders_service.Customers.DTO.CustomerDTO;
import com.raffleease.orders_service.Payments.DTO.PaymentData;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SuccessNotification(

        @NotNull(message = "Must provide an order id")
        Integer orderId,

        @NotNull(message = "Must provide payment data")
        PaymentData paymentData,

        @NotNull(message = "Must provide customer data")
        CustomerDTO customerData
) {
}