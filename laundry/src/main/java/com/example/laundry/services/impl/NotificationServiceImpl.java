package com.example.laundry.services.impl;

import com.example.laundry.services.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public boolean send(String receipt, String message) {
        return false;
    }
}
