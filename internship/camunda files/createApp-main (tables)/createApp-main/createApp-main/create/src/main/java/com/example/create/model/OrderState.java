package com.example.create.model;
public enum OrderState {
    PENDING_PAYMENT,  // Waiting to assign order number
    PENDING_INVENTORY_RESERVATION,  // Waiting to set order status
    CONFIRMED,          // Order is successfully created
    PENDING_VALIDATION_1
}
