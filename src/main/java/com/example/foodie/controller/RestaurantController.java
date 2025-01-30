package com.example.foodie.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodie.entity.Restaurant;
import com.example.foodie.entity.User;
import com.example.foodie.repo.UserRepository;
import com.example.foodie.service.RestaurantService;
import com.example.foodie.utility.AppConstants;
import com.example.foodie.utility.Status;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        Map<String, Object> response = new HashMap<>();
        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
        response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
        response.put("restaurants", restaurants);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getRestaurantById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
            response.put("restaurant", restaurant);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.ErrorMessages.RESTAURANT_NOT_FOUND);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<Map<String, Object>> getRestaurantsByLocation(@PathVariable String location) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByLocation(location);
        Map<String, Object> response = new HashMap<>();
        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
        response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
        response.put("restaurants", restaurants);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createRestaurant(@RequestBody Restaurant restaurant, 
                                                                @RequestHeader("userId") Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() != User.Role.ADMIN) { // ✅ Compare using ENUM
            response.put("status", "FAILURE");
            response.put("message", "Only admins can add restaurants.");
            return ResponseEntity.status(403).body(response);
        }

        restaurant.setOwner(user);
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);

        response.put("status", "SUCCESS");
        response.put("message", "Restaurant added successfully");
        response.put("restaurant", createdRestaurant);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRestaurant(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            restaurantService.deleteRestaurant(id);
            response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.ErrorMessages.RESTAURANT_NOT_FOUND);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
