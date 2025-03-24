package com.example.laundry.repository;

import com.example.laundry.models.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Integer> {
//    boolean send(String receipt, String message);
}
