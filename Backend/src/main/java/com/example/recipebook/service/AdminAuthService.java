package com.example.recipebook.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.recipebook.dto.AdminDto;
import com.example.recipebook.model.User;
import com.example.recipebook.repository.UserRepository;

@Service
public class AdminAuthService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void validate(AdminDto adminDto) {
		User user = userRepo.findByName(adminDto.getName())
				.orElseThrow(()-> new UsernameNotFoundException("Admin not found"));
		if(passwordEncoder.matches(adminDto.getPassword(), user.getPassword())) {
			
		}
	}

}
