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

    public MapleSyrupService(ProductRepository productRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    public MapleSyrupDto getInfo(String productId){
        MapleSyrupDto result = null;
        Product product = productRepository.findById(productId).orElse(null);

        if(product != null){
            Stock stock = stockRepository.findByProductId(productId);
            if(stock != null){
                result = new MapleSyrupDto(product, stock);
            }
        }
        return result;
    }
}
