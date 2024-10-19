package com.rafflease.payments_service.Payments.Services;

import com.rafflease.payments_service.Customers.CustomerDTO;
import com.rafflease.payments_service.Exceptions.CustomExceptions.CustomDeserializationException;
import com.rafflease.payments_service.Exceptions.CustomExceptions.CustomStripeException;
import com.rafflease.payments_service.Exceptions.CustomExceptions.InvalidSignatureException;
import com.rafflease.payments_service.Exceptions.CustomExceptions.StripeWebHookException;
import com.rafflease.payments_service.Kafka.Brokers.NotificationProducer;
import com.rafflease.payments_service.Kafka.Messages.FailureNotificationRequest;
import com.rafflease.payments_service.Kafka.Messages.SuccessNotificationRequest;
import com.rafflease.payments_service.Orders.OrderStatus;
import com.rafflease.payments_service.Payments.DTO.PaymentDTO;
import com.rafflease.payments_service.Payments.DTO.PaymentData;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.MissingEnvironmentVariableException;

import java.math.BigDecimal;
import java.util.Objects;

import static com.rafflease.payments_service.Orders.OrderStatus.CANCELLED;
import static com.rafflease.payments_service.Orders.OrderStatus.FAILED;


@Service
@RequiredArgsConstructor
public class WebHookService {

    private final PaymentsService paymentsService;
    private final NotificationProducer producer;
    private final Environment env;

    public void handleWebHook(String payload, String sigHeader) {
        String webhookKey = getWebhookKey();
        Event event = constructEvent(payload, sigHeader, webhookKey);
        StripeObject stripeObject = deserializeStripeObject(event);
        processPayment(event, stripeObject);
    }

    private String getWebhookKey() {
        String webhookKey = env.getProperty("STRIPE_WEBHOOK_KEY");
        if (Objects.isNull(webhookKey)) {
            throw new MissingEnvironmentVariableException("Environment variable STRIPE_WEBHOOK_KEY is missing.");
        }
        return webhookKey;
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
        if (stripeObject instanceof PaymentIntent) {
            PaymentDTO payment = createPayment((PaymentIntent) stripeObject);
            Long orderId = payment.orderId();

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    handlePaymentSuccess((PaymentIntent) stripeObject, orderId);
                    break;
                case "payment_intent.payment_failed":
                    handlePaymentFailure(orderId);
                    break;
                case "payment_intent.canceled":
                    handlePaymentCanceled(orderId);
                    break;
            }
        }
    }

    private PaymentDTO createPayment(PaymentIntent paymentIntent) {
        return paymentsService.createPayment(
                Long.parseLong(paymentIntent.getMetadata().get("orderId")),
                paymentIntent.getId()
        );
    }

    private void handlePaymentSuccess(PaymentIntent paymentIntent, Long orderId) {
        PaymentData paymentData = getPaymentData(paymentIntent);
        CustomerDTO customerData = getCustomerData(paymentIntent);
        SuccessNotificationRequest request = buildSuccessRequest(orderId, paymentData, customerData);
        producer.sendSuccessNotification(request);
    }

    private void handlePaymentFailure(Long orderId) {
        handlePaymentUnsuccessful(orderId, FAILED);
    }

    private void handlePaymentCanceled(Long orderId) {
        handlePaymentUnsuccessful(orderId, CANCELLED);
    }

    private void handlePaymentUnsuccessful(Long orderId, OrderStatus status) {
        producer.sendFailureNotification(
                FailureNotificationRequest.builder()
                        .orderId(orderId)
                        .status(status)
                        .build()
        );
    }

    private PaymentData getPaymentData(PaymentIntent paymentIntent) {
        PaymentMethod paymentMethod = retrieveStripePaymentMethod(paymentIntent);
        return buildPaymentData(paymentMethod, paymentIntent.getAmount());
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

    private PaymentData buildPaymentData(PaymentMethod paymentMethod, Long amount) {
        return PaymentData.builder()
                .paymentMethod(paymentMethod.getType())
                .total(BigDecimal.valueOf(amount))
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

    private SuccessNotificationRequest buildSuccessRequest(Long orderId, PaymentData paymentData, CustomerDTO customerData) {
        return SuccessNotificationRequest.builder()
                .orderId(orderId)
                .customerData(customerData)
                .paymentData(paymentData)
                .build();
    }
}