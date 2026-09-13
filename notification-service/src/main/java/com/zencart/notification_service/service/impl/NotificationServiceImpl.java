package com.zencart.notification_service.service.impl;

import com.zencart.notification_service.entity.Notification;
import com.zencart.notification_service.repo.NotificationRepo;
import com.zencart.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final NotificationRepo notificationRepo;

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepo.findAll();
    }

    @Override
    public Optional<Notification> getNotificationById(Integer id) {
        return notificationRepo.findById(id);
    }

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepo.save(notification);
    }

    @Override
    public void deleteNotificationById(Integer id) {
        notificationRepo.deleteById(id);
    }
}
