package com.example.laundry.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository {
    boolean send(String receipt, String message);
}
