package com.mathildeclln.sugarshack.service;

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

    public ArrayList<CatalogueItemDto> getCatalogue(MapleType type){
        ArrayList<CatalogueItemDto> result = new ArrayList<>();
        ArrayList<Product> prod = productRepository.findAllByType(type);

        for(Product p: prod){
            Stock stock = stockRepository.findByProductId(p.getId());
            if(stock != null){
                CatalogueItemDto cat = new CatalogueItemDto();
                cat.setId(p.getId());
                cat.setName(p.getName());
                cat.setImage(p.getImage());
                cat.setType(p.getType());
                cat.setPrice(p.getPrice());
                cat.setMaxQty(stock.getStock());
                result.add(cat);
            }
        }
        return result;
    }
}
