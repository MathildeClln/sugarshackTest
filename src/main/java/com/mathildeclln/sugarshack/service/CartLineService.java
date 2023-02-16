package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.CartLineDto;
import com.mathildeclln.sugarshack.model.OrderLine;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.repository.OrderLineRepository;
import com.mathildeclln.sugarshack.repository.ProductRepository;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartLineService {
    @Autowired
    private OrderLineRepository orderRepo;
    @Autowired
    private ProductRepository prodRepo;

    public ArrayList<CartLineDto> getCart(){
        ArrayList<CartLineDto> result = new ArrayList<>();
        List<OrderLine> order = orderRepo.findAll();

        for(OrderLine line: order){
            Product p = prodRepo.findById(Integer.valueOf(line.getProductId()))
                                .orElse(null);
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

    public void addToCart(String productId){
        OrderLine line;

        line = orderRepo.findByProductId(productId);

        if(line != null){
            int qty = line.getQty()+1;
            line.setQty(qty);
            orderRepo.save(line);
        }
        else{
            line = new OrderLine();

            OrderLine max = orderRepo.findFirstByOrderByIdDesc();
            if(max != null){
                line.setId(max.getId()+1);
            }else{
                line.setId(1);
            }

            line.setQty(1);
            line.setProductId(productId);
            line.setError("");
            line.setValid(false);
            orderRepo.save(line);
        }
    }

    @Transactional
    public void removeFromCart(String productId){
        if(orderRepo.existsByProductId(productId)){
            orderRepo.deleteByProductId(productId);
        }
    }
}
