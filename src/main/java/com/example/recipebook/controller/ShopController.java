package com.example.recipebook.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.recipebook.dto.ShopResponseDto;
import com.example.recipebook.model.Recipe;
import com.example.recipebook.model.Shop;
import com.example.recipebook.model.User;
import com.example.recipebook.repository.RecipeRepository;
import com.example.recipebook.repository.ShopRepository;
import com.example.recipebook.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/recipebook")
public class ShopController {

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private ShopRepository shopRepo;

	@Autowired
	private UserRepository userRepo;

	@GetMapping("/shop/{id}")
	public ResponseEntity<Object> showShops(@PathVariable long id, @RequestHeader("Authorization") String authHeader) {
		String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
		User user = userRepo.findByToken(token);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid user token"));
		}

		Optional<Recipe> recipeOptional = recipeRepo.findById(id);
		if (!recipeOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Recipe not found!!"));
		}

		Recipe recipe = recipeOptional.get();
		List<String> recipeIngridients = Arrays.asList(recipe.getIngridients().split(","));

		List<Shop> allShops = shopRepo.findAll();
		List<ShopResponseDto> shopResList = new ArrayList<>();
		for (Shop shop : allShops) {

			List<String> matchingIng = new ArrayList<>();
			List<String> shopIng = new ArrayList<>();

			for (String ing : shop.getIngridients()) {
				shopIng.add(ing.trim());
			}

			for (String ing : recipeIngridients) {
				if (shopIng.contains(ing.trim())) {
					matchingIng.add(ing.trim());
				}
			}
			if (!matchingIng.isEmpty()) {
				ShopResponseDto shopResponseDto = new ShopResponseDto(shop.getShopName(), shop.getLocation(),
						matchingIng);
				shopResList.add(shopResponseDto);
			}
		}

		return ResponseEntity.ok(shopResList);
	}
}