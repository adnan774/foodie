package com.example.foodie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.foodie.entity.Dish;
import com.example.foodie.repo.DishRepository;

import java.util.List;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));
    }

    public List<Dish> getDishesByRestaurant(Long restaurantId) {
        return dishRepository.findByRestaurantId(restaurantId);
    }

    public List<Dish> searchDishesByName(String name) {
        return dishRepository.findByNameContainingIgnoreCase(name);
    }

    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}

