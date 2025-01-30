package com.example.foodie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.foodie.dto.OrderRequest;
import com.example.foodie.dto.OrderItemRequest;
import com.example.foodie.entity.Order;
import com.example.foodie.entity.OrderItem;
import com.example.foodie.entity.Dish;
import com.example.foodie.entity.User;
import com.example.foodie.repo.OrderRepository;
import com.example.foodie.repo.OrderItemRepository;
import com.example.foodie.repo.DishRepository;
import com.example.foodie.repo.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private DishRepository dishRepository;
    @Autowired private UserRepository userRepository;

    public Order createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order = orderRepository.save(order); // ✅ Save Order first!

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest item : request.getItems()) {
            Dish dish = dishRepository.findById(item.getDishId())
                .orElseThrow(() -> new RuntimeException("Dish not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); // ✅ Set Order properly
            orderItem.setDish(dish);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(dish.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems); // ✅ Save order items
        order.setOrderItems(orderItems);

        return order;
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
