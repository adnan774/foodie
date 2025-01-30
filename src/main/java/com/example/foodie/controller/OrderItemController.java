package com.example.foodie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.foodie.entity.OrderItem;
import com.example.foodie.service.OrderItemService;
import com.example.foodie.utility.AppConstants;
import com.example.foodie.utility.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemService.getAllOrderItems();
        Map<String, Object> response = new HashMap<>();
        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
        response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
        response.put("orderItems", orderItems);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderItemById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            OrderItem orderItem = orderItemService.getOrderItemById(id);
            response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
            response.put("orderItem", orderItem);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, "Order item not found");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderItemsByOrder(@PathVariable Long orderId) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrder(orderId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Operation completed successfully");
        response.put("orderItems", orderItems);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrderItem(@RequestBody OrderItem orderItem) {
        OrderItem createdOrderItem = orderItemService.createOrderItem(orderItem);
        Map<String, Object> response = new HashMap<>();
        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
        response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
        response.put("orderItem", createdOrderItem);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrderItem(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            orderItemService.deleteOrderItem(id);
            response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, "Order item not found");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
