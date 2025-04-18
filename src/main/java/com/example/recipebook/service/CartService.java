package com.example.recipebook.service;

import com.example.recipebook.dto.UpdateCartDto;
import com.example.recipebook.dto.ViewCartDto;
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

import java.util.List;
import java.util.stream.Collectors;

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

    public String addToCart(String token, Long itemId, int quantity){
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

        List<CartItem> cartItems = cartItemRepo.findByCart(cart);
        for (CartItem cartItem : cartItems){
            if(cartItem.getItem().getId().equals(itemId)){
                return "Item already exists!!";
            }
        }

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setQuantity(quantity);

        cartItemRepo.save(cartItem);
        return "Item successfully added!!";
    }

    public List<ViewCartDto> viewCart(User user) {
        Cart cart = cartRepo.findByUser(user);
        List<CartItem> cartItems = cartItemRepo.findByCart(cart);

        return cartItems.stream()
                .map(ci -> new ViewCartDto(
                        ci.getItem().getName(),
                        ci.getQuantity()
                )).collect(Collectors.toList());
    }

    public String updateQuantity(User user, UpdateCartDto dto) {
        Cart cart = cartRepo.findByUser(user);
        if(cart == null){
            throw new RuntimeException("Cart Empty");
        }

        CartItem cartItem = cartItemRepo.findByCartAndItemId(cart, dto.getItemId());
        if(cartItem == null){
            throw new RuntimeException("Item not found");
        }

        cartItem.setQuantity(dto.getQuantity());
        cartItemRepo.save(cartItem);
        return "Updated Successfully";
    }

    public String removeItem(User user, Long itemId) {

        Cart cart = cartRepo.findByUser(user);
        if(cart == null) throw new RuntimeException("Cart Empty");

        CartItem cartItem = cartItemRepo.findByCartAndItemId(cart, itemId);
        cartItemRepo.delete(cartItem);

        return "item delted!!";
    }
}
