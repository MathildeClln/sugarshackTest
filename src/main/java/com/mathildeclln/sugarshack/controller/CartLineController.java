package com.mathildeclln.sugarshack.controller;

import com.mathildeclln.sugarshack.dto.CartLineDto;
import com.mathildeclln.sugarshack.service.CartLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/cart")
public class CartLineController {
    @Autowired
    private CartLineService cartService;

    @GetMapping
    public ArrayList<CartLineDto> getCart(){
        return cartService.getCart();
    }

    @PutMapping
    public void addToCart(String productId){
        cartService.addToCart(productId);
    }

    @DeleteMapping
    public void removeFromCart(String productId){
        cartService.removeFromCart(productId);
    }
}
