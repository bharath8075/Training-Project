package com.example.recipebook.controller;

import java.security.KeyStore.PasswordProtection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.recipebook.dto.AdminDto;
import com.example.recipebook.exception.RecipeNotFoundException;
import com.example.recipebook.model.Recipe;
import com.example.recipebook.model.User;
import com.example.recipebook.repository.RecipeRepository;
import com.example.recipebook.repository.UserRepository;
import com.example.recipebook.service.AdminAuthService;
@Controller
@RequestMapping("/admin/recipebook")
public class AdminController {
	


	@Autowired
	private AdminAuthService adminService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

//  	@GetMapping("/login")
//	public String showLoginPage() {
//		return "Login";
//	}
	@GetMapping("/login")
	public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("loginError", "Invalid credentials. Please try again.");
		}
		return "Login"; // ✅ Ensure this matches your login page name
	}
	


	@PostMapping("/login")
	public String processLogin(@ModelAttribute("admin") User admin, Model model) {

	    User existingAdmin = userRepo.findByEmail(admin.getEmail());

	    if (existingAdmin == null) {
	        model.addAttribute("loginError", "User not found!");
	        return "Login";
	    }


	    if (!existingAdmin.getRole().equals("ROLE_ADMIN")) {
	        model.addAttribute("loginError", "Access Denied! Only Admins can log in.");
	        return "Login";
	    }


	    if (!passwordEncoder.matches(admin.getPassword(), existingAdmin.getPassword())) {
	        model.addAttribute("loginError", "Invalid password!");
	        return "Login";
	    }

	    System.out.println("✅ Login successful!");
	    return "redirect:/admin/recipebook/dashboard";
	}

//	@PostMapping("/login")
//	public String processLogin(@ModelAttribute("admin") User admin, Model model) {
//		User existingAdmin = userRepo.findByEmail(admin.getEmail());
//
//		if (existingAdmin == null) {
//	        model.addAttribute("LoginError", "User not found!");
//	        return "Login";
//	    }
//		
//		if (!existingAdmin.getRole().equals("ROLE_ADMIN")) {
//			model.addAttribute("LoginError", "Access Denied! Only Admins can log in.");
//			System.out.println("Access Denied! Only Admins can log in.");
//			return "Login";
//		}
//
//		if (!passwordEncoder.matches(admin.getPassword(), existingAdmin.getPassword())) {
//			model.addAttribute("LoginError", "Incorrect Password");
//			System.out.println("Incorrect Password");
//			return "Login";
//		}
//
//		return "redirect:/admin/recipebook/dashboard"; // Redirect to Admin Dashboard after successful login
//	}

	@GetMapping("/dashboard")
	public String backToDashboard(Model model) {
		List<User> users = userRepo.findAll();
		model.addAttribute("users", users);
		return "Dashboard";
	}

	@GetMapping("/recipedetails/{id}")
	public String showRecipee(@PathVariable Long id, Model model) {
		List<Recipe> recipes = recipeRepo.findByUserId(id);
		model.addAttribute("recipes", recipes);
		return "RecipeDetails";
	}


	@GetMapping("/AllRecipeDetails")
	public String showAllRecipe(Model model) {
		List<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "AllRecipe";
	}

	@GetMapping("recipe/view/{id}")
	public String diplayRecipeDetails(@PathVariable Long id, Model model) {
		Recipe recipe = recipeRepo.findById(id).orElseThrow(()-> new RecipeNotFoundException(id));
		model.addAttribute("recipe", recipe);
		return "View";
	}
	
	@GetMapping("/userblock/{id}") //The method below block the user
	public String blockUser(@PathVariable Long id) {
		User existingUser = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not exist"));
		existingUser.setBlocked(true);
		userRepo.save(existingUser);
		return "redirect:/admin/recipebook/dashboard";
	}
	

	@GetMapping("/user_unblock/{id}") //The method below unBlock the user
	public String unBlockUser(@PathVariable Long id) {
		User existingUser = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		existingUser.setBlocked(false);
		userRepo.save(existingUser);
		return "redirect:/admin/recipebook/dashboard";
	}
	
	@GetMapping("/recipeblock/{id}")
	public String blockRecipe(@PathVariable Long id) {
		Recipe existingRecipe = recipeRepo.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
		existingRecipe.setEnabled(false);
		recipeRepo.save(existingRecipe);
		return "redirect:/admin/recipebook/AllRecipeDetails";
	}
	@GetMapping("/recipe_block/{id}")
	public String blockRecipee(@PathVariable Long id) {
		Recipe existingRecipe = recipeRepo.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
		User user = existingRecipe.getUser(); 
		existingRecipe.setEnabled(false);
		recipeRepo.save(existingRecipe);
		return "redirect:/admin/recipebook/recipedetails/"+user.getId();
	}
	
	@GetMapping("recipeunblock/{id}")
	public String recipeUnBlock(@PathVariable Long id) {
		Recipe existingRecipe = recipeRepo.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
		existingRecipe.setEnabled(true);
		recipeRepo.save(existingRecipe);
		return "redirect:/admin/recipebook/AllRecipeDetails";
	}
	
	@GetMapping("recipe_unblock/{id}")
	public String recipeUnBlockk(@PathVariable Long id) {
		Recipe existingRecipe = recipeRepo.findById(id).orElseThrow(() -> new RecipeNotFoundException(id));
		User user = existingRecipe.getUser();
		existingRecipe.setEnabled(true);
		recipeRepo.save(existingRecipe);
		return "redirect:/admin/recipebook/recipedetails/"+user.getId();
	}

}
