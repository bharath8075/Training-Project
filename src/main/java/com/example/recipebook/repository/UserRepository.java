package com.example.recipebook.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.recipebook.model.User;


public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByToken(String token);
	
	boolean existsByToken(String token);

	Optional<User> findByName(String name);
//	String findByUserName(String name);
}
