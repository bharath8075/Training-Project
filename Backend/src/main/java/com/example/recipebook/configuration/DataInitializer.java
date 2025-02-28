package com.example.recipebook.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.recipebook.model.User;
import com.example.recipebook.repository.UserRepository;

@Component
public class DataInitializer implements ApplicationRunner{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;



	    @Override
	    public void run(ApplicationArguments args) {
	    	Optional<User> adminCheck = Optional.ofNullable((userRepo.findByEmail("admin@gmail.com")));
	        if (adminCheck.isEmpty()) {
	            User admin = new User();
	            admin.setName("admin");
	            admin.setEmail("admin@gmail.com");
	            admin.setPassword(passwordEncoder.encode("123")); // Encrypt password
	            admin.setRole("ROLE_ADMIN");

	            userRepo.save(admin);
	            System.out.println("Admin user created successfully!");
	        }
	    }
	}