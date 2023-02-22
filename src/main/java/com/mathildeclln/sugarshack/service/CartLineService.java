package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.CartLineDto;
import com.mathildeclln.sugarshack.exception.InvalidQuantityException;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.OrderLine;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.repository.OrderLineRepository;
import com.mathildeclln.sugarshack.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartLineService {
    @Autowired
    private OrderLineRepository orderLineRepository;
    @Autowired
    private ProductRepository productRepository;

    public CartLineService(OrderLineRepository orderLineRepository, ProductRepository productRepository){
        this.orderLineRepository = orderLineRepository;
        this.productRepository = productRepository;
    }

    public ArrayList<CartLineDto> getCart() throws ProductNotFoundException {
        ArrayList<CartLineDto> result = new ArrayList<>();
        List<OrderLine> orderLines = orderLineRepository.findAll();

        for (OrderLine orderLine : orderLines) {
            Product product = productRepository.findById(orderLine.getProductId()).orElse(null);

            if (product != null) {
                CartLineDto c = new CartLineDto(product, orderLine);

                result.add(c);
            } else {
                throw new ProductNotFoundException(orderLine.getProductId());
            }
        }
        return result;
    }

    public CartLineDto getCartLineById(String productId){
        CartLineDto result = null;

        OrderLine orderLine = orderLineRepository.findByProductId(productId);
        Product product = productRepository.findById(productId).orElse(null);

        if(product != null & orderLine != null){
            result = new CartLineDto(product, orderLine);
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
        OrderLine line = orderLineRepository.findByProductId(productId);

        if(line != null){
            if(newQty > 0){
                line.setQty(newQty);
                orderLineRepository.save(line);
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
