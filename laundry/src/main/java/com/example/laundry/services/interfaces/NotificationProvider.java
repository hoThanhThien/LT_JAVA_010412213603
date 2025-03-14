package com.example.laundry.services.interfaces;

public interface NotificationProvider {
    boolean send(String receipt, String message);
}
