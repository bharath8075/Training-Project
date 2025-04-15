package com.example.recipebook.model;

import javax.persistence.*;
import java.util.List;


@Entity
public class Shop {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String shopName;
	private String location;

	@OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
	private List<Item> items;

	Shop(){
		super();
	}

	public Shop(int id, List<Item> items, String location, String shopName) {
		this.id = id;
		this.items = items;
		this.location = location;
		this.shopName = shopName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
