package com.zencart.user_service.auth.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
