package com.rafflease.payments_service.Payments.Services;

import com.rafflease.payments_service.Exceptions.CustomExceptions.DataBaseHandlingException;
import com.rafflease.payments_service.Payments.DTO.PaymentDTO;
import com.rafflease.payments_service.Payments.Mappers.PaymentsMapper;
import com.rafflease.payments_service.Payments.Model.Payment;
import com.rafflease.payments_service.Payments.Repositories.IPaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentsService {
    private final IPaymentsRepository repository;
    private final PaymentsMapper mapper;

    public PaymentDTO createPayment(Long orderId, String paymentIntentId) {
        Payment savedPayment = savePayment(orderId, paymentIntentId);
        return mapPayment(savedPayment);
    }

    public Payment savePayment(Long orderId, String paymentIntentId) {
        try {
            return repository.save(
                    Payment.builder()
                            .orderId(orderId)
                            .stripePaymentId(paymentIntentId)
                            .build()
            );
        } catch (DataAccessException exp) {
            throw new DataBaseHandlingException("Error saving new payment information: " + exp.getMessage());
        }
    }

    public PaymentDTO mapPayment(Payment payment) {
        return mapper.fromPayment(payment);
    }

}