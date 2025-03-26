package com.example.recipebook.model;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String shopName;
	private String location;
	
	@ElementCollection
	private List<String> ingridients;

	public Shop(int id, String shopName, String location, List<String> ingridients) {
		super();
		this.id = id;
		this.shopName = shopName;
		this.location = location;
		this.ingridients = ingridients;
	}
	public Shop() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<String> getIngridients() {
		return ingridients;
	}

	public void setIngridients(List<String> ingridients) {
		this.ingridients = ingridients;
	}
	
}
