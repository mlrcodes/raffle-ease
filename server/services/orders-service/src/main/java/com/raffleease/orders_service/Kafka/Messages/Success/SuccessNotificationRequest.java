package com.raffleease.orders_service.Kafka.Messages.Success;

import com.raffleease.orders_service.Customers.CustomerDTO;
import com.raffleease.orders_service.Orders.DTO.OrderData;
import com.raffleease.orders_service.Payments.DTO.PaymentData;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SuccessNotificationRequest(

        @NotNull(message = "Must provide payment data")
        PaymentData paymentData,

        @NotNull(message = "Must provide customer data")
        CustomerDTO customerData,

        @NotNull(message = "Must provide order data")
        OrderData orderData

) {
}