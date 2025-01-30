package com.example.foodie.dto;

public class OrderItemRequest {
    private Long dishId;
    private int quantity;

    // Getters and Setters
    public Long getDishId() { return dishId; }
    public void setDishId(Long dishId) { this.dishId = dishId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
