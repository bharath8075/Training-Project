package com.example.recipebook.security;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.recipebook.model.User;
import com.example.recipebook.repository.UserRepository;

@Service
public class TokenGenerator {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int TOKEN_LENGTH = 60;
	private static final SecureRandom RANDOM = new SecureRandom();

	private String generateRandomString() {
		StringBuilder stringBuilder = new StringBuilder(TOKEN_LENGTH);

		for (int i = 0; i < TOKEN_LENGTH; i++) {
			int randomInt = RANDOM.nextInt(CHARACTERS.length());
			stringBuilder.append(CHARACTERS.charAt(randomInt));
		}
		return stringBuilder.toString();
	}

	public String generateToken(String email, String password) {
		User user = userRepository.findByEmail(email);

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			String token;
			do {
				token = generateRandomString();
			} while (userRepository.existsByToken(token));
			user.setToken(token);
			userRepository.save(user);
			return token; // Where are we returning the token ask CHECHI!!!
		}
		return null; // Why are we returnin null and where are we setting the null
	}

	// validating the token
	public boolean validateToken(String token) {
		User user = userRepository.findByToken(token);
		return user != null; // Why are we setting the user not null and where are we returning
	}

}
