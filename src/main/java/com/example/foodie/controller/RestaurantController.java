package com.example.foodie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.foodie.entity.Restaurant;
import com.example.foodie.service.RestaurantService;
import com.example.foodie.utility.AppConstants;
import com.example.foodie.utility.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

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
    public ResponseEntity<Map<String, Object>> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        Map<String, Object> response = new HashMap<>();
        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
        response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
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
