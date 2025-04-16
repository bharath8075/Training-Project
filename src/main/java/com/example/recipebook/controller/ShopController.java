package com.example.recipebook.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.recipebook.model.CartItem;
import com.example.recipebook.service.CartService;
import com.example.recipebook.service.ViewCartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

	@Autowired
	private CartService cartService;

	@GetMapping("/add-Shop-Details")
	public List<Shop> showAddShop() {
		List<Shop> allShops = shopRepo.findAll();
		return allShops;
	}

	@PostMapping("/add-Shop-Details")
	public String addShop(@ModelAttribute("shopDetails") Shop shop, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("loginError", "Please fill all fields correctly!");
			return "AddShopDetails";
		}
		shopRepo.save(shop);
		return "redirect:/admin/recipebook/add-Shop-Details";
	}

	@GetMapping("/shop/{id}")
	public ResponseEntity<Object> showShops(@PathVariable long id, @RequestHeader("Authorization") String authHeader) {
		String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
		User user = userRepo.findByToken(token);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid user token"));
		}

		Optional<Recipe> recipeOptional = recipeRepo.findById(id);
		if (recipeOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Recipe not found!!"));
		}

		Recipe recipe = recipeOptional.get();
		String[] recipeIngridients = recipe.getIngridients().split(",");

		List<Shop> allShops = shopRepo.findAll();
		List<ShopResponseDto> shopResList = new ArrayList<>();
		for (Shop shop : allShops) {

//			List<String> matchingIng = new ArrayList<>();
//			List<String> shopIng = new ArrayList<>();


//			for (String ing : shop.getIngridients()) {
//				shopIng.add(ing.trim());
//			}
//
//			for (String ing : recipeIngridients) {
//				if (shopIng.contains(ing.trim())) {
//					matchingIng.add(ing.trim());
//				}
//			}

			List<String> shopItemNames = shop.getItems().stream()
					.map(item -> item.getName().trim())
					.collect(Collectors.toList());
			List<String> matchingIngredients = Arrays.stream(recipeIngridients)
					.map(String::trim)
					.filter(shopItemNames::contains)
					.collect(Collectors.toList());

			if (!matchingIngredients.isEmpty()) {
				ShopResponseDto shopResponseDto = new ShopResponseDto(shop.getShopName(), shop.getLocation(),
						matchingIngredients);
				shopResList.add(shopResponseDto);
			}
		}

		return ResponseEntity.ok(shopResList);
	}

	@PostMapping("/add-shop")
	public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String authHeader, @RequestParam Long itemId, @RequestParam int quantity){
		String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
		User user = userRepo.findByToken(token);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid user token"));
		}
		String result = cartService.addToCart(token, itemId, quantity);
		return ResponseEntity.ok(Map.of("message", result));
	}

	@GetMapping("/view-cart")
	public ResponseEntity<?> viewCart(@RequestHeader("Authorization") String authHeader){
		String token = (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
		User user = userRepo.findByToken(token);
		List<ViewCartDto> cartItems = cartService.viewCart(user);

		return ResponseEntity.ok(cartItems);
	}
}