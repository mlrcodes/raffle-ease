package com.raffleease.payments_service.Payments.Services;

import com.raffleease.common_models.DTO.Orders.CreateSessionRequest;
import com.raffleease.common_models.DTO.Raffles.RaffleDTO;
import com.raffleease.common_models.Exceptions.CustomExceptions.CustomStripeException;
import com.raffleease.payments_service.Feign.Clients.RafflesClient;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class StripeService {
    private final RafflesClient rafflesClient;

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Value("${CLIENT_HOST}")
    private String clientHost;

    @Value("${CLIENT_PATH}")
    private String clientPath;

    public String createSession(CreateSessionRequest request) {
        Stripe.apiKey = stripeSecretKey;
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
                                .putMetadata("raffleId", request.raffleId().toString())
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
                                                .setUnitAmount(raffle.ticketPrice().multiply(BigDecimal.valueOf(100)).longValue())
                                                .build()
                                )
                                .setQuantity(request.quantity())
                                .build()
                )
                .setReturnUrl(clientHost + clientPath + raffle.id())
                .build();
    }

    public String getPublicKey() {
        return stripePublicKey;
    }

    private Session buildSession(SessionCreateParams params) {
        try {
            return Session.create(params);
        } catch (StripeException exp) {
            throw new CustomStripeException("Error creating checkout session: " + exp.getMessage());
        }
    }

    private RaffleDTO getRaffleInfo(Long raffleId) {
        return rafflesClient.get(raffleId);
    }
}