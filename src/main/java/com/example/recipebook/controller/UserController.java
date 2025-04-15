package com.example.recipebook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.recipebook.dto.DtoPassword;
import com.example.recipebook.model.User;
import com.example.recipebook.repository.UserRepository;
import com.example.recipebook.security.TokenGenerator;

//import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/recipebook")
public class UserController {
	
	@Autowired
	private TokenGenerator tokenGen;
	
	 @Autowired  
	 private UserRepository userRepository;
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public String addUser(@RequestBody User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		userRepository.save(user);
		return user.getName()+" has registered";
	}
	
	
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody User user){
		User existingUser = userRepository.findByEmail(user.getEmail());
		
		if(existingUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not found");
		}
		
		if(existingUser.isBlocked()) {
			return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is blocked. Contact Bharath");
		}
		
		
		if(passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
			String token = tokenGen.generateToken(user.getEmail(), user.getPassword());
			
			return ResponseEntity.ok(Map.of("token", token, "Message", "Successfully logged in"));
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Password");
	}

	@PutMapping("/change_password") //Ask chechi for change password logic stuck here
	public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String authHeader, 
			@RequestBody DtoPassword dtoPassword){
		String token = authHeader!=null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
		User existingUser = userRepository.findByToken(token);
		
		if(existingUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not found");
		}
		if(!passwordEncoder.matches(dtoPassword.getCurrentPassword(), existingUser.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credential");
		}
		
		existingUser.setPassword(passwordEncoder.encode(dtoPassword.getNewPassword()));
		userRepository.save(existingUser);
		
		return ResponseEntity.ok("Password changed Successfully!");
		
	}
}
