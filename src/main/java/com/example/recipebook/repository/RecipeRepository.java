package com.example.recipebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.recipebook.model.Recipe;

@Repository 
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
	
	List<Recipe> findByUserId(Long userId);

	List<Recipe> findByIsEnabledTrue();

}
