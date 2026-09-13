package com.zencart.notification_service.controller;

import com.zencart.notification_service.dto.EmailDetails;
import com.zencart.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sendSimpleMail")
    public String sendSimpleMail(EmailDetails emailDetails){
        return emailService.sendSimpleMail(emailDetails);
    }

    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(EmailDetails emailDetails){
        return emailService.sendMailWithAttachment(emailDetails);
    }

    @PostMapping("/sendMail")
    public String sendMail(@RequestParam(value = "file", required = false)MultipartFile[] files,
                           @RequestParam String to,
                           @RequestParam String[] cc,
                           @RequestParam String subject,
                           @RequestParam String body){
        return emailService.sendMail(files, to, cc, subject, body);

    }
}
