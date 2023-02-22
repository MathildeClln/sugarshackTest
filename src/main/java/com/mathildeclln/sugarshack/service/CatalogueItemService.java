package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.dto.CatalogueItemDto;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.model.Stock;
import com.mathildeclln.sugarshack.repository.ProductRepository;
import com.mathildeclln.sugarshack.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CatalogueItemService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockRepository stockRepository;

    public CatalogueItemService() {
    }

    public CatalogueItemService(ProductRepository productRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

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
}
