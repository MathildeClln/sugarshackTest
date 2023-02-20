package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.CartLineDto;
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

    public ArrayList<CartLineDto> getCart(){
        ArrayList<CartLineDto> result = new ArrayList<>();
        List<OrderLine> order = orderLineRepository.findAll();

        for(OrderLine line: order){
            Product p = productRepository.findById(line.getProductId()).orElse(null);

            if(p != null){
                CartLineDto c = new CartLineDto();
                c.setProductId(line.getProductId());
                c.setName(p.getName());
                c.setImage(p.getImage());
                c.setPrice(p.getPrice());
                c.setQty(line.getQty());

                result.add(c);
            }
        }
        return result;
    }

    public CartLineDto getCartById (String productId){
        CartLineDto result = null;

        OrderLine orderLine = orderLineRepository.findByProductId(productId);
        Product product = productRepository.findById(productId).orElse(null);

        if(product != null & orderLine != null){
            result = new CartLineDto(product, orderLine);
        }
        return result;
    }

    public void addToCart(String productId){
        OrderLine line;

        line = orderLineRepository.findByProductId(productId);

        if(line != null){
            int qty = line.getQty()+1;
            line.setQty(qty);
            orderLineRepository.save(line);
        }
        else{
            line = new OrderLine();
            line.setQty(1);
            line.setProductId(productId);
            orderLineRepository.save(line);
        }
    }

    @Transactional
    public void removeFromCart(String productId){
        if(orderLineRepository.existsByProductId(productId)){
            orderLineRepository.deleteByProductId(productId);
        }
    }

    public void changeQty(String productId, int newQty){
        OrderLine line = orderLineRepository.findByProductId(productId);
        if(line != null){
            line.setQty(newQty);
            orderLineRepository.save(line);
        }
    }
}
