package com.example.recipebook.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.recipebook.dto.UserNameRecipe;
import com.example.recipebook.dto.UserRecipeResponse;
import com.example.recipebook.exception.RecipeNotFoundException;
import com.example.recipebook.model.Recipe;
import com.example.recipebook.model.User;
import com.example.recipebook.repository.RecipeRepository;
import com.example.recipebook.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/recipebook")
public class RecipeController {

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private UserRepository userRepo;

	@PostMapping("/add")
	public ResponseEntity<?> addRecipe(@RequestPart("recipe") Recipe recipe,
			@RequestPart("image") MultipartFile imageFile, @RequestHeader("Authorization") String authHeader) {
		try {
			// Extract token
			String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;

			// Find user by token
			User user = userRepo.findByToken(token);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user token");
			}

			// Set user to recipe
			recipe.setUser(user); // This sets the user_id in the recipe

			// Handle image upload
			if (imageFile != null && !imageFile.isEmpty()) {
				recipe.setImage(imageFile.getBytes());
			}

			recipe.setEnabled(true);
			recipeRepo.save(recipe);
			return ResponseEntity.ok("Recipe added successfully!");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving image: " + e.getMessage());
		}
	}



	@GetMapping("/recipes")
	public List<Map<String, Object>> getAllRecipes(@RequestHeader("Authorization") String authHeader) {
		String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
		User user = userRepo.findByToken(token);

		List<Recipe> recipes = recipeRepo.findByIsEnabledTrue();
		List<Map<String, Object>> recipeDetails = new ArrayList<>();

		for (Recipe recipe : recipes) {
			Map<String, Object> recipeMap = new HashMap<>();
			recipeMap.put("id", recipe.getId());
			recipeMap.put("title", recipe.getTitle());
			recipeMap.put("difficulty", recipe.getDifficulty());
			recipeMap.put("cookingTime", recipe.getCookingTime());
			recipeMap.put("procedures", recipe.getProcedures());
			recipeMap.put("ingridients", recipe.getIngridients());
			recipeMap.put("user", recipe.getUser().getName()); // Fetch username
			recipeMap.put("viewCount", recipe.getViewCount());
			recipeMap.put("image", Base64.getEncoder().encodeToString(recipe.getImage())); // Convert image to Base64

			recipeDetails.add(recipeMap);
		}

		return recipeDetails;
	}

	// Getting recipe by user
	@GetMapping("/recipes/user")
	public ResponseEntity<UserRecipeResponse> listByUser(@RequestHeader("Authorization") String authHeader) {
		String token = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
		}

		User user = userRepo.findByToken(token);
		List<Recipe> recipe  = recipeRepo.findByUserId(user.getId());

		UserRecipeResponse response = new UserRecipeResponse(user, recipe);

		return ResponseEntity.ok(response);
	}

	// Getting specific recipe details
	@GetMapping("recipes/{id}")
	public ResponseEntity<?> viewRecipe(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
// Extract token
		String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
		Recipe recipe = recipeRepo.findById(id).orElse(null);
		User user = userRepo.findById(recipe.getUser().getId()).orElse(null);
		
		User VUser = (token!=null) ? userRepo.findByToken(token) : null;
		
		if(VUser.getId() != user.getId()) {
			recipe.setViewCount(recipe.getViewCount()+1);
			recipeRepo.save(recipe);
		}
		
		UserNameRecipe unr = new UserNameRecipe(recipe, user);
		return ResponseEntity.ok(unr);

	}
//	

	// Method for deleting
	@DeleteMapping("/delete/{id}")
	String deleteRecipe(@PathVariable Long id) {
		if (!recipeRepo.existsById(id)) {
			throw new RecipeNotFoundException(id);
		}

		recipeRepo.deleteById(id);
		return "Recipe with the id " + id + " has been deleted.";
	}

	// Method for Updating
//	@PutMapping("edit/{id}")
//	public ResponseEntity<Recipe> editRecipe(
//			@RequestPart("recipe") Recipe updatedRecipe,
//			@RequestPart(value = "image", required = false) MultipartFile image,
//			@PathVariable Long id) {
//
//		return recipeRepo.findById(id).map(recipe -> {
//			System.out.println(recipe);
//			recipe.setTitle(updatedRecipe.getTitle());
//			recipe.setIngridients(updatedRecipe.getIngridients());
//			recipe.setCookingTime(updatedRecipe.getCookingTime());
//			recipe.setDifficulty(updatedRecipe.getDifficulty());
//			recipe.setProcedures(updatedRecipe.getProcedures());
//			recipe.setAbout(updatedRecipe.getAbout());
//			
//			byte[] imageData = image.getBytes();
//			recipe.setImage(imageData);
//		});
//		
//		return ResponseEntity.ok(recipeRepo.save(recipe));
//	}
	@PutMapping("edit/{id}")
	public ResponseEntity<Recipe> editRecipe(
	        @PathVariable Long id,
	        @RequestPart("recipe") Recipe updatedRecipe,
	        @RequestPart(value = "image", required = false) MultipartFile imageFile) {

	    return recipeRepo.findById(id).map(recipe -> {
	        recipe.setTitle(updatedRecipe.getTitle());
	        recipe.setIngridients(updatedRecipe.getIngridients());
	        recipe.setCookingTime(updatedRecipe.getCookingTime());
	        recipe.setDifficulty(updatedRecipe.getDifficulty());
	        recipe.setProcedures(updatedRecipe.getProcedures());
	        recipe.setAbout(updatedRecipe.getAbout());

	        // Handling image upload
	        if (imageFile != null && !imageFile.isEmpty()) {
	            try {
	                byte[] imageData = imageFile.getBytes();
	                recipe.setImage(imageData);
	            } catch (IOException e) {
	                throw new RuntimeException("Error processing image file", e);
	            }
	        }

	        return ResponseEntity.ok(recipeRepo.save(recipe));
	    }).orElseThrow(() -> new RecipeNotFoundException(id));
	}

}
