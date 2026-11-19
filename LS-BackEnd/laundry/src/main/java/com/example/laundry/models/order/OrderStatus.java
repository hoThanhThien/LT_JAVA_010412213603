package com.example.laundry.models.order;

public enum OrderStatus {
    IN_PROGRESS,
    DONE;

    public boolean canTransitionTo(OrderStatus target) {
        if (target == null || this == target) {
            return false;
        }
        return this == IN_PROGRESS && target == DONE;
    }
}
