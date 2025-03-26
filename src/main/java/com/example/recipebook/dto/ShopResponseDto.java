package com.example.recipebook.dto;

import java.util.List;

public class ShopResponseDto {

	private String shopName;
	private String location;
	private List<String> ingridients;
	
	public ShopResponseDto(String shopName, String location, List<String> ingridients) {
		super();
		this.shopName = shopName;
		this.location = location;
		this.ingridients = ingridients;
	}

	public ShopResponseDto() {
		// TODO Auto-generated constructor stub
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
