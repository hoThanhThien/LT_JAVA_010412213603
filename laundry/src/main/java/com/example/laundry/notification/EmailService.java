package com.example.laundry.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String verificationLink) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Xác thực tài khoản của bạn!!");
        mailMessage.setText("Vui lòng nhấp vào đường link để xác thực tài khoản của bạn: " + verificationLink);
        mailSender.send(mailMessage);
    }
}
