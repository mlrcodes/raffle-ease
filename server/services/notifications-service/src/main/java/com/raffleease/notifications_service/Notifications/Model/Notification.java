package com.raffleease.notifications_service.Notifications.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private NotificationType notificationType;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime notificationDate;

    @Column(unique = true)
    private Long paymentId;

    @Column(unique = true)
    private Long orderId;

}