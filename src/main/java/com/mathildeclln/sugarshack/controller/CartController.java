package com.mathildeclln.sugarshack.controller;

import com.mathildeclln.sugarshack.dto.CartLineDto;
import com.mathildeclln.sugarshack.service.CartLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartLineService cartLineService;

    @GetMapping
    public ArrayList<CartLineDto> getCart(){

        return cartLineService.getCart();
    }

    @PutMapping
    public void addToCart(String productId){

        cartLineService.addToCart(productId);
    }

    @DeleteMapping
    public void removeFromCart(String productId){

        cartLineService.removeFromCart(productId);
    }

    @PatchMapping
    public void changeQty(String productId, int newQty){

        cartLineService.changeQty(productId, newQty);
    }
}
