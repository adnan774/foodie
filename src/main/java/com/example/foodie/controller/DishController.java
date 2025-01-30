package com.example.foodie.controller;

import java.math.BigDecimal;
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

import com.example.foodie.entity.Dish;
import com.example.foodie.entity.Restaurant;
import com.example.foodie.entity.User;
import com.example.foodie.repo.RestaurantRepository;
import com.example.foodie.repo.UserRepository;
import com.example.foodie.service.DishService;
import com.example.foodie.utility.AppConstants;
import com.example.foodie.utility.Status;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService dishService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;

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
    public ResponseEntity<Map<String, Object>> createDish(@RequestBody Map<String, Object> dishData, 
                                                          @RequestHeader("userId") Long userId) {
        Map<String, Object> response = new HashMap<>();

        // ✅ Fetch user properly
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() != User.Role.ADMIN) { 
            response.put("status", "FAILURE");
            response.put("message", "Only admins can add dishes.");
            return ResponseEntity.status(403).body(response);
        }

        // ✅ Extract values from the received JSON
        String dishName = (String) dishData.get("name");
        BigDecimal price = new BigDecimal(dishData.get("price").toString());
        Map<String, Object> restaurantData = (Map<String, Object>) dishData.get("restaurant");

        if (restaurantData == null || !restaurantData.containsKey("id")) {
            response.put("status", "FAILURE");
            response.put("message", "Invalid restaurant selection.");
            return ResponseEntity.badRequest().body(response);
        }

        Long restaurantId = Long.valueOf(restaurantData.get("id").toString());
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));

        // ✅ Create the Dish object with correct Restaurant
        Dish dish = new Dish();
        dish.setName(dishName);
        dish.setPrice(price);
        dish.setRestaurant(restaurant);

        Dish createdDish = dishService.createDish(dish);
        response.put("status", "SUCCESS");
        response.put("message", "Dish added successfully");
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
