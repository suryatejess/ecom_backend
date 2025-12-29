package com.example.ecom_backend.services;

import com.example.ecom_backend.entities.Cart;
import com.example.ecom_backend.entities.CartItem;
import com.example.ecom_backend.entities.Product;
import com.example.ecom_backend.repositories.CartItemRepository;
import com.example.ecom_backend.repositories.CartRepository;
import com.example.ecom_backend.repositories.ProductRepository;
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

    public List<CartItem> findAllProducts(){
        Cart cart = getOrCreateCart();

        List<CartItem> cartItems = cart.getCartItems();
        return cartItems;
    }

    public Cart getOrCreateCart(){
        return cartRepository
                .findFirstByOrderByIdAsc()
                .orElseGet(() -> cartRepository.save(new Cart()));
    }

//    public void clearCart(){
//        Cart cart = getOrCreateCart();
//        cart.setCartItems(null);
//        cartRepository.save(cart);
//    }

    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    public void addProductToCart(Long productId, int quantity){
        Cart cart = getOrCreateCart();

        Product product =  prodRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("product not found"));

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElse(null);

        if(cartItem != null){
            // already in cart
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        else{
            // new product
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);

            cart.getCartItems().add(newItem);
        }

        cartRepository.save(cart);
    }
}
