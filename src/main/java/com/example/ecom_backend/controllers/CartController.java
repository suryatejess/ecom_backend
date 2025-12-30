package com.example.ecom_backend.controllers;

import com.example.ecom_backend.dtos.CartItemDTO;
import com.example.ecom_backend.entities.CartItem;
import com.example.ecom_backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /*
    - [x] add product to cart. ( if we want to decrement, frontend has to send negative quantity value )
        quantity should be there. if quantity is not sent, default it to 0
        validate stock exists

    - [ ] remove product from cart
    - [x] be able to modify the quantity of a particular product ( product ofc )
    - [x] clear cart
     */

    @PostMapping("/")
    public void addToCart(@RequestBody CartItemDTO dto) {
        // TODO: check if there are enough products. right now, we are directly updating the quantity. in the new thing, there should be something to validate that the present quantities are not exceeding the available quantity of the product

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Long productId = dto.getProductId();
        int quantity = dto.getQuantity();

        cartService.addProductToCart(username, productId, quantity);
    }

    @DeleteMapping("/")
    public void clearCart(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        cartService.clearCart(username);
    }

    @GetMapping("/")
    public List<CartItemDTO> getCartItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<CartItem> cartItems = cartService.getCartProductsByUsername(username);

        List<CartItemDTO> cartItemDTOs = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            CartItemDTO cartItemDTO = new CartItemDTO();

            cartItemDTO.setQuantity(cartItem.getQuantity());
            cartItemDTO.setProductId(cartItem.getProduct().getId());

            cartItemDTOs.add(cartItemDTO);
        }

        return cartItemDTOs;
    }

}
