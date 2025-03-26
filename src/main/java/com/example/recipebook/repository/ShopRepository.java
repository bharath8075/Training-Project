package com.example.recipebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.recipebook.model.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer>{

}
