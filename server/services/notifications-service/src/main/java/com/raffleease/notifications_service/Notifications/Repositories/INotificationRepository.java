package com.raffleease.notifications_service.Notifications.Repositories;

import com.raffleease.notifications_service.Notifications.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {
}