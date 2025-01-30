package com.example.foodie.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference 
    private List<OrderItem> orderItems;

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

	public Order(Long id, User user, List<OrderItem> orderItems, LocalDateTime orderDate) {
		super();
		this.id = id;
		this.user = user;
		this.orderItems = orderItems;
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

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", user=" + user + ", orderItems=" + orderItems + ", orderDate=" + orderDate + "]";
	}
    
}


    


