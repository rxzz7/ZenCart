package com.zencart.notification_service.service;

import com.zencart.notification_service.dto.EmailDetails;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(EmailDetails details);
    String sendMail(MultipartFile[] files, String to, String[] cc, String subject, String body);
}
