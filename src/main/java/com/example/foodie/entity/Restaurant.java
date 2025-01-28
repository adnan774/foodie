package com.example.foodie.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String cuisine;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

	public Restaurant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Restaurant(Long id, String name, String location, String cuisine, User owner) {
		super();
		this.id = id;
		this.name = name;
		this.location = location;
		this.cuisine = cuisine;
		this.owner = owner;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCuisine() {
		return cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", location=" + location + ", cuisine=" + cuisine
				+ ", owner=" + owner + "]";
	}

    
}

