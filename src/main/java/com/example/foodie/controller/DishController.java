package com.example.foodie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.foodie.entity.Dish;
import com.example.foodie.service.DishService;
import com.example.foodie.utility.AppConstants;
import com.example.foodie.utility.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDishes() {
        List<Dish> dishes = dishService.getAllDishes();
        Map<String, Object> response = new HashMap<>();
        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
        response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
        response.put("dishes", dishes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDishById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Dish dish = dishService.getDishById(id);
            response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
            response.put("dish", dish);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, "Dish not found");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Map<String, Object>> getDishesByRestaurant(@PathVariable Long restaurantId) {
        List<Dish> dishes = dishService.getDishesByRestaurant(restaurantId);
        Map<String, Object> response = new HashMap<>();
        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
        response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
        response.put("dishes", dishes);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDish(@RequestBody Dish dish) {
        Dish createdDish = dishService.createDish(dish);
        Map<String, Object> response = new HashMap<>();
        response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
        response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
        response.put("dish", createdDish);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDish(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            dishService.deleteDish(id);
            response.put(AppConstants.ResponseKeys.STATUS, Status.SUCCESS.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, AppConstants.SuccessMessages.OPERATION_SUCCESS);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put(AppConstants.ResponseKeys.STATUS, Status.FAILURE.name());
            response.put(AppConstants.ResponseKeys.MESSAGE, "Dish not found");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
