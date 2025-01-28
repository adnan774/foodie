package com.example.foodie.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_date", updatable = false)
    private LocalDateTime orderDate;

    @PrePersist
    protected void onOrderDate() {
        this.orderDate = LocalDateTime.now();
    }

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Order(Long id, User user, LocalDateTime orderDate) {
		super();
		this.id = id;
		this.user = user;
		this.orderDate = orderDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", user=" + user + ", orderDate=" + orderDate + "]";
	}

    
}

