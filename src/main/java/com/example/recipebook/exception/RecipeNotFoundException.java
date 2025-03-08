package com.example.recipebook.exception;


public class RecipeNotFoundException extends RuntimeException {
	public RecipeNotFoundException(Long id) {
		super("Could not find the product with the id "+ id);
	}

}
