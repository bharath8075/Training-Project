package com.example.recipebook.dto;

import java.util.List;

import com.example.recipebook.model.Recipe;
import com.example.recipebook.model.User;

public class UserRecipeResponse {
	
	private User user;
	private List<Recipe> recipes;
	
	public UserRecipeResponse(User user, List<Recipe> recipe) {
		super();
		this.user = user;
		this.recipes = recipe;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}
	
	

}
