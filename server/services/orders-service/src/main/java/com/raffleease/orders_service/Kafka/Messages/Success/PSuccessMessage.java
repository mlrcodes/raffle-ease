package com.raffleease.orders_service.Kafka.Messages.Success;

import com.raffleease.orders_service.Customers.CustomerDTO;
import com.raffleease.orders_service.Payments.DTO.PaymentData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PSuccessMessage(

        @NotNull(message = "Must provide an order id")
        @Min(value = 1, message = "Order id must be a positive number")
        Long orderId,

        @NotNull(message = "Must provide payment data")
        @Valid
        PaymentData paymentData,

        @NotNull(message = "Must provide customer data")
        CustomerDTO customerData
) {
}