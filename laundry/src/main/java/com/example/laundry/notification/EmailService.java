package com.example.laundry.notification;

import com.example.laundry.models.order.Order;
import com.example.laundry.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    private OrderService orderService;

    public void sendVerificationEmail(String to, String verificationLink) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Xác thực tài khoản của bạn!!");
        mailMessage.setText("Vui lòng nhấp vào đường link để xác thực tài khoản của bạn: " + verificationLink);
        mailSender.send(mailMessage);
    }
    public void notifyCustomer(Long orderId, String message) {
        Optional<Order> orderOpt = orderService.getOrderById(orderId); // Sử dụng phương thức đúng
        if (orderOpt.isPresent() && orderOpt.get().getCustomer() != null) {
            String customerEmail = orderOpt.get().getCustomer().getEmail(); // Giả sử Customer có phương thức getEmail()
            sendEmail(customerEmail, message); // Gọi phương thức gửi email
        }
    }

    public void sendEmail(String to, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Thông báo về đơn hàng");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}

