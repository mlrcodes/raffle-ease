package com.rafflease.payments_service.Payments.Services;

import com.rafflease.payments_service.Exceptions.CustomExceptions.CustomStripeException;
import com.rafflease.payments_service.Exceptions.CustomExceptions.RaffleRetrievalException;
import com.rafflease.payments_service.Payments.DTO.CreateSessionRequest;
import com.rafflease.payments_service.Raffles.Clients.RafflesClient;
import com.rafflease.payments_service.Raffles.DTO.RaffleDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class StripeService {
    private final RafflesClient rafflesClient;
    private final Environment env;
    private static final Logger logger = LoggerFactory.getLogger(StripeService.class);

    public String createSession(CreateSessionRequest request) {
        logger.info("Tickets quantity:" + request.quantity());
        Stripe.apiKey = getApiKey();
        RaffleDTO raffle = getRaffleInfo(request.raffleId());
        SessionCreateParams params = buildSessionParams(request, raffle);
        Session session = buildSession(params);
        return session.getClientSecret();
    }

    private SessionCreateParams buildSessionParams(CreateSessionRequest request, RaffleDTO raffle) {
        return SessionCreateParams.builder()
                .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setPhoneNumberCollection(
                        SessionCreateParams.PhoneNumberCollection.builder().setEnabled(true).build()
                )
                .setCustomerCreation(
                        SessionCreateParams.CustomerCreation.ALWAYS
                )
                .setPaymentIntentData(
                        SessionCreateParams.PaymentIntentData.builder()
                                .putMetadata("orderId", request.orderId().toString())
                                .build()
                )
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Ticket for raffle " + raffle.title())
                                                                .build()
                                                )
                                                .setUnitAmount(raffle.ticketPrice().longValue())
                                                .build()
                                )
                                .setQuantity(request.quantity())
                                .build()
                )
                .setReturnUrl("http://localhost:4200")
                .setSuccessUrl("http://localhost:4200")
                .build();
    }

    private Session buildSession(SessionCreateParams params) {
        try {
            return Session.create(params);
        } catch (StripeException exp) {
            throw new CustomStripeException("Error creating checkout session: " + exp.getMessage());
        }
    }

    private RaffleDTO getRaffleInfo(Long raffleId) {
        try {
            return rafflesClient.getRaffleInfo(raffleId);
        } catch (RuntimeException exp) {
            throw new RaffleRetrievalException("Raffle retrieval failed: " + exp.getMessage());
        }
    }

    public String getPublicKey() {
        String publicKey = env.getProperty("STRIPE_PUBLIC_KEY");
        if (Objects.isNull(publicKey)) {
            throw new IllegalArgumentException("Error trying to retrieve Stripe Public Key");
        }
        return publicKey;
    }

    public String getApiKey() {
        String apiKey = env.getProperty("STRIPE_SECRET_KEY");
        if (Objects.isNull(apiKey)) {
            throw new IllegalArgumentException("Error trying to retrieve Stripe API Key");
        }
        return apiKey;
    }
}