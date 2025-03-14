package com.example.laundry.services.interfaces;

public interface OrderHandle {
    void updateOrderStatus(int orderId, String status);
    void notifyCustomer(int orderId, String message);
}
