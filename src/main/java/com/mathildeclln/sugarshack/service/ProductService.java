package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.CatalogueItemDto;
import com.mathildeclln.sugarshack.dto.MapleSyrupDto;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.model.Stock;
import com.mathildeclln.sugarshack.repository.ProductRepository;
import com.mathildeclln.sugarshack.repository.StockRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;

    public ArrayList<CatalogueItemDto> getCatalogue(MapleType type){
        ArrayList<CatalogueItemDto> result = new ArrayList<>();
        ArrayList<Product>          products = productRepository.findAllByType(type);

        for(Product product: products){
            Stock stock = stockRepository.findByProductId(product.getId());
            if(stock != null){
                CatalogueItemDto catalogueItem = new CatalogueItemDto(product, stock);

                result.add(catalogueItem);
            }
            else{
                throw new ProductNotFoundException(product.getId());
            }
        }
        return result;
    }

    public MapleSyrupDto getInfo(String productId){
        MapleSyrupDto       result;
        Optional<Product> product = productRepository.findById(productId);
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
