package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.MapleSyrupDto;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.model.Stock;
import com.mathildeclln.sugarshack.repository.ProductRepository;
import com.mathildeclln.sugarshack.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MapleSyrupService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;

    public MapleSyrupDto getInfo(String productId){
        MapleSyrupDto result = new MapleSyrupDto();
        Product prod = productRepository.findById(productId);

        if(prod != null){
            Stock stock = stockRepository.findByProductId(productId);
            if(stock != null){
                result.setId(prod.getId());
                result.setName(prod.getName());
                result.setImage(prod.getImage());
                result.setType(prod.getType());
                result.setPrice(prod.getPrice());
                result.setDescription(prod.getDescription());
                result.setStock(stock.getStock());

                return result;
            }
        }
        return null;
    }
}
