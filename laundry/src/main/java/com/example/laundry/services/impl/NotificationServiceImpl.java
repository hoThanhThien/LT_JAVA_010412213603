package com.example.laundry.services.impl;

import com.example.laundry.repository.NotificationRepository;
import com.example.laundry.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

//    @Override
//    public boolean send(String receipt, String message) {
//        return notificationRepository.send(receipt, message);
//    }
}
