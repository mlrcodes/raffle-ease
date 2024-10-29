package com.raffleease.notifications_service.Kafka.Consumers;

import com.raffleease.common_models.DTO.Kafka.OrderSuccess;
import com.raffleease.notifications_service.Email.EmailService;
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

    @KafkaListener(topics = "order-success-topic", groupId = "notifications-group")
    public void consumeSuccessfulOrderNotification(
            OrderSuccess orderSuccess
    ) throws MessagingException {
        repository.save(
                Notification.builder()
                        .orderId(orderSuccess.order().orderId())
                        .notificationType(NotificationType.ORDER_SUCCESS)
                        .build()
        );
        emailService.sendOrderSuccessNotification(orderSuccess);
    }
}
