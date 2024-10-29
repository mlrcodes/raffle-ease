package com.raffleease.payments_service.Payments.Services;

import com.raffleease.common_models.DTO.Payment.PaymentDTO;
import com.raffleease.common_models.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.raffleease.payments_service.Payments.Mappers.PaymentsMapper;
import com.raffleease.payments_service.Payments.Model.Payment;
import com.raffleease.payments_service.Payments.Repositories.IPaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class PaymentsService {
    private final IPaymentsRepository repository;
    private final PaymentsMapper mapper;

    public PaymentDTO createPayment(
            Long orderId,
            String paymentMethod,
            BigDecimal total,
            String paymentIntentId
    ) {
        Payment payment = buildPayment(orderId, paymentMethod, total, paymentIntentId);
        Payment savedPayment = savePayment(payment);
        return mapper.fromPayment(savedPayment);
    }

    public Payment savePayment(Payment payment) {
        try {
            return repository.save(payment);
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Error saving new payment information: " + exp.getMessage());
        }
    }

    private Payment buildPayment(
            Long orderId,
            String paymentMethod,
            BigDecimal total,
            String paymentIntentId
    ) {
        return Payment.builder()
                .orderId(orderId)
                .paymentMethod(paymentMethod)
                .total(total)
                .stripePaymentId(paymentIntentId)
                .build();
    }
}