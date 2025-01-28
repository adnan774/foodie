package com.example.foodie.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dishes")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

	public Dish() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Dish(Long id, String name, BigDecimal price, Restaurant restaurant) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.restaurant = restaurant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "Dish [id=" + id + ", name=" + name + ", price=" + price + ", restaurant=" + restaurant + "]";
	}

    
}

