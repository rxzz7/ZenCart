package com.zencart.notification_service.service;

import com.zencart.notification_service.entity.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<Notification> getAllNotifications();
    Optional<Notification> getNotificationById(Integer id);
    Notification saveNotification(Notification notification);
    void deleteNotificationById(Integer id);
}
