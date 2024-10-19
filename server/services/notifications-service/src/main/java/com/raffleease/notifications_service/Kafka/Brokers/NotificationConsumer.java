package com.raffleease.notifications_service.Kafka.Brokers;

import com.raffleease.notifications_service.Email.EmailService;
import com.raffleease.notifications_service.Kafka.Messages.Success.SuccessNotification;
import com.raffleease.notifications_service.Notifications.Model.Notification;
import com.raffleease.notifications_service.Notifications.Model.NotificationType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.raffleease.notifications_service.Notifications.Repositories.INotificationRepository;

@RequiredArgsConstructor
@Service
public class NotificationConsumer {
    private final INotificationRepository repository;
    private final EmailService emailService;

    @KafkaListener(topics = "n-success-topic", groupId = "notifications-group")
    public void consumeSuccessfulOrderNotification(
            SuccessNotification notificationRequest
    ) throws MessagingException {
        repository.save(
                Notification.builder()
                        .orderId(notificationRequest.orderData().orderId())
                        .notificationType(NotificationType.ORDER_SUCCESS)
                        .build()
        );
        emailService.sendOrderSuccessNotification(notificationRequest);
    }
}
