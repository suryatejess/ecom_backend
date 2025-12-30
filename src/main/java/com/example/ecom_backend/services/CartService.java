package com.example.ecom_backend.services;

import com.example.ecom_backend.entities.AppUser;
import com.example.ecom_backend.entities.Cart;
import com.example.ecom_backend.entities.CartItem;
import com.example.ecom_backend.entities.Product;
import com.example.ecom_backend.repositories.CartItemRepository;
import com.example.ecom_backend.repositories.CartRepository;
import com.example.ecom_backend.repositories.ProductRepository;
import com.example.ecom_backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository prodRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepo userRepo;

    public Cart getOrCreateCartByUsername(String username) {
        AppUser user = userRepo.findByUsername(username);
        if(user==null){
            throw new RuntimeException("user not found");
        }

        return cartRepository
                .findByAppUser(user)
                .orElseGet(
                        () -> {
                            Cart cart = new Cart();
                            cart.setAppUser(user);
                            return  cartRepository.save(cart);
                        }
                );
    }

    public List<CartItem> getCartProductsByUsername(String username) {
        Cart cart = getOrCreateCartByUsername(username);
        return cart.getCartItems();
    }

    public void addProductToCart(String username, Long productId, int quantity) {

        Cart cart = getOrCreateCartByUsername(username);

        Product product = prodRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getCartItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    public void clearCart(String username) {
        Cart cart = getOrCreateCartByUsername(username);
        cart.setCartItems(null);
        cartRepository.save(cart);
    }

}
