package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.MapleSyrupDto;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.model.Stock;
import com.mathildeclln.sugarshack.repository.ProductRepository;
import com.mathildeclln.sugarshack.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


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
        MapleSyrupDto       result;
        Optional<Product>   product = productRepository.findById(productId);
        Stock               stock = stockRepository.findByProductId(productId);

        if(product.isEmpty() || stock == null) {
            throw new ProductNotFoundException(productId);
        }
        else {
            result = new MapleSyrupDto(product.get(), stock);
        }
        return result;
    }
}
