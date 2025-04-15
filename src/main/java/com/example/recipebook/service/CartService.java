package com.example.recipebook.service;

import com.example.recipebook.model.Cart;
import com.example.recipebook.model.CartItem;
import com.example.recipebook.model.Item;
import com.example.recipebook.model.User;
import com.example.recipebook.repository.CartItemRepository;
import com.example.recipebook.repository.CartRepository;
import com.example.recipebook.repository.ItemRepository;
import com.example.recipebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemRepository itemRepo;

    public void addToCart(String token, Long itemId){
        User user = userRepo.findByToken(token);
        if (user == null) throw new RuntimeException("Invalid User");

        Item item = itemRepo.findById(itemId)
                .orElseThrow(()-> new RuntimeException("Item not found"));

        Cart cart = cartRepo.findByUser(user);
        if(cart == null){
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepo.save(cart);
        }
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);

        cartItemRepo.save(cartItem);
    }
}
