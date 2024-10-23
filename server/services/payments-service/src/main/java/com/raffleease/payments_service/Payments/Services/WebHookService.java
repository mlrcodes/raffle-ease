package com.raffleease.payments_service.Payments.Services;

import com.raffleease.payments_service.Customers.CustomerDTO;
import com.raffleease.payments_service.Exceptions.CustomExceptions.CustomDeserializationException;
import com.raffleease.payments_service.Exceptions.CustomExceptions.CustomStripeException;
import com.raffleease.payments_service.Exceptions.CustomExceptions.InvalidSignatureException;
import com.raffleease.payments_service.Exceptions.CustomExceptions.StripeWebHookException;
import com.raffleease.payments_service.Kafka.Brokers.Producers.TestProducer;
import com.raffleease.payments_service.Kafka.Messages.PSuccessMessage;
import com.raffleease.payments_service.Payments.DTO.PaymentDTO;
import com.raffleease.payments_service.Payments.DTO.PaymentData;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.MissingEnvironmentVariableException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class WebHookService {

    private final PaymentsService paymentsService;
    /*
    private final SuccessProducer successProducer;
    private final FailureProducer failureProducer;

     */
    private final TestProducer testProducer;
    private final Environment env;

    private static final Logger logger = LoggerFactory.getLogger(WebHookService.class);

    public void handleWebHook(String payload, String sigHeader) {
        String webhookKey = env.getProperty("STRIPE_WEBHOOK_KEY");

        if (Objects.isNull(webhookKey)) {
            logger.info("Error retrieving webhook key");
            throw new MissingEnvironmentVariableException("Environment variable STRIPE_WEBHOOK_KEY is missing.");
        }


        Event event;
        try {
            logger.info("Constructing event");
            event = Webhook.constructEvent(payload, sigHeader, webhookKey);
            logger.info("Stripe event constructed successfully.");
        } catch (SignatureVerificationException exp) {
            throw new InvalidSignatureException("Invalid signature for Stripe webhook provided: " + exp.getMessage());
        } catch (Exception exp) {
            throw new StripeWebHookException("Unexpected error processing Stripe webhook: " + exp.getMessage());
        }


        if (event.getType().startsWith("payment_intent")) {
            logger.info("Received event type: " + event.getType());
        }

        Optional<StripeObject> stripeObjectOpt = event.getDataObjectDeserializer().getObject();
        if (stripeObjectOpt.isEmpty()) {
            logger.info("Deserialization failed.");
            throw new CustomDeserializationException("Failed to deserialize Stripe object from event");
        }
        StripeObject stripeObject = stripeObjectOpt.get();
        logger.info("Stripe object deserialized successfully");

        processPayment(event, stripeObject);
    }


    private void processPayment(Event event, StripeObject stripeObject) {

        logger.info("Initializing payment processing for order");

        if (stripeObject instanceof PaymentIntent) {
            logger.info("Stripe object is payment intent");

            PaymentDTO payment = createPayment((PaymentIntent) stripeObject);

            logger.info("PAYMENT DTO: {}", payment);

            Long orderId = payment.orderId();

            logger.info("Processing payment for order ID: " + orderId);

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    logger.info("PAYMENT SUCCESS");
                    handlePaymentSuccess((PaymentIntent) stripeObject, orderId);
                    break;
                    /*
                case "payment_intent.payment_failed":
                    handlePaymentFailure(orderId);
                    break;
                case "payment_intent.canceled":
                    handlePaymentCanceled(orderId);
                    break;

                     */
            }
        }
    }

    private PaymentDTO createPayment(PaymentIntent paymentIntent) {
        logger.info("CREATING PAYMENT DTO");
        return paymentsService.createPayment(
                Long.parseLong(paymentIntent.getMetadata().get("orderId")),
                paymentIntent.getId()
        );
    }

    private void handlePaymentSuccess(PaymentIntent paymentIntent, Long orderId) {
        /*
        logger.info("HANDLING PAYMENT SUCCESS");

        PaymentData paymentData = getPaymentData(paymentIntent);
        logger.info("PAYMENT DATA: {}", paymentData);

        CustomerDTO customerData = getCustomerData(paymentIntent);
        logger.info("CUSTOMER DATA: {}", customerData);


        PSuccessMessage request = buildSuccessRequest(orderId, paymentData, customerData);
        logger.info("SUCCESS NOTIFICATION REQUEST: {}", request);


        successProducer.notifySuccess(request);
        logger.info("NOTIFICATION SUCCESSFULLY SENT");

    }



    private void handlePaymentFailure(Long orderId) {
        handlePaymentUnsuccessful(orderId, FAILED);
    }

    private void handlePaymentCanceled(Long orderId) {
        handlePaymentUnsuccessful(orderId, CANCELLED);
    }



    private void handlePaymentUnsuccessful(Long orderId, OrderStatus status) {

        failureProducer.notifyFailure(
                PFailureMessage.builder()
                        .orderId(orderId)
                        .status(status)
                        .build()
        );

         */
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

    private PSuccessMessage buildSuccessRequest(Long orderId, PaymentData paymentData, CustomerDTO customerData) {
        return PSuccessMessage.builder()
                .orderId(orderId)
                .customerData(customerData)
                .paymentData(paymentData)
                .build();
    }

    public String sendMessage(MessageDTO message) {
        return testProducer.sendMessage(message);
    }
}