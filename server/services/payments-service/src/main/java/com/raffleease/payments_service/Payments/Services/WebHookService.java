package com.raffleease.payments_service.Payments.Services;

import com.raffleease.common_models.DTO.Customers.CustomerDTO;
import com.raffleease.common_models.DTO.Kafka.PaymentFailure;
import com.raffleease.common_models.DTO.Kafka.PaymentSuccess;
import com.raffleease.common_models.DTO.Orders.OrderStatus;
import com.raffleease.common_models.DTO.Payment.PaymentDTO;
import com.raffleease.common_models.Exceptions.CustomExceptions.CustomDeserializationException;
import com.raffleease.common_models.Exceptions.CustomExceptions.CustomStripeException;
import com.raffleease.common_models.Exceptions.CustomExceptions.InvalidSignatureException;
import com.raffleease.common_models.Exceptions.CustomExceptions.StripeWebHookException;
import com.stripe.exception.SignatureVerificationException;
import com.raffleease.payments_service.Kafka.Producers.FailureProducer;
import com.raffleease.payments_service.Kafka.Producers.SuccessProducer;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.MissingEnvironmentVariableException;
import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WebHookService {
    private final PaymentsService paymentsService;
    private final SuccessProducer successProducer;
    private final FailureProducer failureProducer;
    private final Environment env;

    public void handleWebHook(String payload, String sigHeader) {
        String webhookKey = getWebhookKey();
        Event event = constructEvent(payload, sigHeader, webhookKey);
        StripeObject stripeObject = deserializeStripeObject(event);
        processPayment(event, stripeObject);
    }

    private Event constructEvent(String payload, String sigHeader, String webhookKey) {
        try {
            return Webhook.constructEvent(payload, sigHeader, webhookKey);
        } catch (SignatureVerificationException exp) {
            throw new InvalidSignatureException("Invalid signature for Stripe webhook provided: " + exp.getMessage());
        } catch (Exception exp) {
            throw new StripeWebHookException("Unexpected error processing Stripe webhook: " + exp.getMessage());
        }
    }

    private StripeObject deserializeStripeObject(Event event) {
        return event.getDataObjectDeserializer().getObject()
                .orElseThrow(() -> new CustomDeserializationException("Failed to deserialize Stripe object from event"));
    }

    private void processPayment(Event event, StripeObject stripeObject) {
        if (stripeObject instanceof PaymentIntent paymentIntent) {
            PaymentDTO payment = createPayment(paymentIntent);
            Long orderId = payment.orderId();
            Long raffleId = Long.parseLong(paymentIntent.getMetadata().get("raffleId"));

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    handlePaymentSuccess((PaymentIntent) stripeObject, orderId);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentFailure(orderId, raffleId);
                    break;
                case "payment_intent.canceled":
                    handlePaymentCanceled(orderId, raffleId);
                    break;
            }
        }
    }

    private PaymentDTO createPayment(PaymentIntent paymentIntent) {
        return paymentsService.createPayment(
                Long.parseLong(paymentIntent.getMetadata().get("orderId")),
                paymentIntent.getId(),
                BigDecimal.valueOf(paymentIntent.getAmount()),
                paymentIntent.getPaymentMethod()
        );
    }

    private void handlePaymentSuccess(PaymentIntent paymentIntent, Long orderId) {
        PaymentDTO payment = getPaymentData(paymentIntent, orderId);
        CustomerDTO customer = getCustomerData(paymentIntent);
        PaymentSuccess request = buildSuccessRequest(orderId, payment, customer);
        successProducer.notifySuccess(request);
    }

    private void handlePaymentFailure(Long orderId, Long raffleId) {
        handlePaymentUnsuccessful(orderId, OrderStatus.FAILED, raffleId);
    }

    private void handlePaymentCanceled(Long orderId, Long raffleId) {
        handlePaymentUnsuccessful(orderId, OrderStatus.CANCELED, raffleId);
    }

    private void handlePaymentUnsuccessful(Long orderId, OrderStatus status, Long raffleId) {
        failureProducer.notifyFailure(
                PaymentFailure.builder()
                        .orderId(orderId)
                        .status(status)
                        .raffleId(raffleId)
                        .build()
        );
    }

    private PaymentDTO getPaymentData(PaymentIntent paymentIntent, Long orderId) {
        PaymentMethod paymentMethod = retrieveStripePaymentMethod(paymentIntent);
        return buildPaymentDTO(
                orderId,
                paymentMethod,
                paymentIntent.getAmount(),
                paymentIntent.getId()
        );
    }

    private CustomerDTO getCustomerData(PaymentIntent paymentIntent) {
        Customer customer = retrieveStripeCustomerData(paymentIntent);
        return buildCustomerData(customer);
    }

    private PaymentMethod retrieveStripePaymentMethod(PaymentIntent paymentIntent) {
        try {
            return PaymentMethod.retrieve(paymentIntent.getPaymentMethod());
        } catch (StripeException exp) {
            throw new CustomStripeException("Error retrieving payment data from stripe: " + exp.getMessage());
        }
    }

    private Customer retrieveStripeCustomerData(PaymentIntent paymentIntent) {
        try {
            return Customer.retrieve(paymentIntent.getCustomer());
        } catch (StripeException exp) {
            throw new CustomStripeException("Error retrieving customer data from stripe: " + exp.getMessage());
        }
    }

    private PaymentDTO buildPaymentDTO(
            Long orderId,
            PaymentMethod paymentMethod,
            Long amount,
            String paymentIntentId
    ) {
        return PaymentDTO.builder()
                .orderId(orderId)
                .paymentMethod(paymentMethod.getType())
                .total(BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100)))
                .paymentIntentId(paymentIntentId)
                .build();
    }

    private CustomerDTO buildCustomerData(Customer customer) {
        return CustomerDTO.builder()
                .stripeId(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhone())
                .build();
    }

    private PaymentSuccess buildSuccessRequest(Long orderId, PaymentDTO payment, CustomerDTO customer) {
        return PaymentSuccess.builder()
                .orderId(orderId)
                .customer(customer)
                .payment(payment)
                .build();
    }

    private String getWebhookKey() {
        String webhookKey = env.getProperty("STRIPE_WEBHOOK_KEY");
        if (Objects.isNull(webhookKey)) {
            throw new MissingEnvironmentVariableException("Environment variable STRIPE_WEBHOOK_KEY is missing.");
        }
        return webhookKey;
    }
}