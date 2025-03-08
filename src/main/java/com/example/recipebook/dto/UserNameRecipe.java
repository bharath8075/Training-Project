package com.example.recipebook.dto;

import com.example.recipebook.model.Recipe;
import com.example.recipebook.model.User;

public class UserNameRecipe {

	private Recipe recipe;
	private User user;
	public UserNameRecipe(Recipe recipe, User user) {
		super();
		this.recipe = recipe;
		this.user = user;
	}
	public Recipe getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
