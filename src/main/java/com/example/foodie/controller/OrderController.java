package com.example.foodie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.foodie.dto.OrderRequest;
import com.example.foodie.entity.Order;
import com.example.foodie.service.OrderService;
import com.example.foodie.utility.AppConstants;
import com.example.foodie.utility.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        Map<String, Object> response = new HashMap<>();
        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
        response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
        response.put("orders", orders);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order order = orderService.getOrderById(id);
            response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
            response.put("order", order);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, "Order not found");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        Map<String, Object> response = new HashMap<>();
        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
        response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
        response.put("orders", orders);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order createdOrder = orderService.createOrder(request);
            response.put("status", "SUCCESS");
            response.put("message", "Order placed successfully");
            response.put("order", createdOrder);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "FAILURE");
            response.put("message", "Error placing order: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            orderService.deleteOrder(id);
            response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, "Order not found");
            return ResponseEntity.badRequest().body(response);
        }
    }
}


