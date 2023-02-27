package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.CartLineDto;
import com.mathildeclln.sugarshack.exception.InvalidQuantityException;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.OrderLine;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.repository.OrderLineRepository;
import com.mathildeclln.sugarshack.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {
    @Autowired
    private OrderLineRepository orderLineRepository;
    @Autowired
    private ProductRepository productRepository;

    public ArrayList<CartLineDto> getCart() throws ProductNotFoundException {
        ArrayList<CartLineDto>  result = new ArrayList<>();
        List<OrderLine>         orderLines = orderLineRepository.findAll();

        for (OrderLine orderLine : orderLines) {
            Optional<Product> optionalProduct = productRepository.findById(orderLine.getProductId());

            if (optionalProduct.isEmpty()) {
                throw new ProductNotFoundException(orderLine.getProductId());
            }
            else {
                CartLineDto cartLine = new CartLineDto(optionalProduct.get(), orderLine);

                result.add(cartLine);
            }
        }
        return result;
    }

    public CartLineDto getCartLineById(String productId){
        CartLineDto result;
        OrderLine           orderLine = orderLineRepository.findByProductId(productId);
        Optional<Product>   product = productRepository.findById(productId);

        if(product.isEmpty() || orderLine == null) {
            throw new ProductNotFoundException(productId);
        }
        else {
            result = new CartLineDto(product.get(), orderLine);
        }
        return result;
    }

    public void addToCart(String productId){
        OrderLine orderLine;

        if(productRepository.existsById(productId)){
            if(orderLineRepository.existsByProductId(productId)){
                orderLine = orderLineRepository.findByProductId(productId);
                orderLine.setQty(orderLine.getQty()+1);
                orderLineRepository.save(orderLine);
            }
            else{
                orderLine = new OrderLine(productId, 1);
                orderLineRepository.save(orderLine);
            }
        }
        else {
            throw new ProductNotFoundException(productId);
        }
    }

    @Transactional
    public void removeFromCart(String productId){
        if(orderLineRepository.existsByProductId(productId)){
            orderLineRepository.deleteByProductId(productId);
        }
        else{
            throw new ProductNotFoundException(productId);
        }
    }

    public void changeQty(String productId, int newQty){
        OrderLine orderLine = orderLineRepository.findByProductId(productId);

        if(orderLine != null){
            if(newQty > 0){
                orderLine.setQty(newQty);
                orderLineRepository.save(orderLine);
            } else if (newQty == 0) {
                orderLineRepository.deleteByProductId(productId);
            }
            else {
                throw new InvalidQuantityException(newQty);
            }
        }
        else{
            throw new ProductNotFoundException(productId);
        }
    }
}
