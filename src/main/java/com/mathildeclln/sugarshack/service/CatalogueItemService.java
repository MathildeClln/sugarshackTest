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
    private ProductRepository prodrepo;
    @Autowired
    private StockRepository stockrepo;

    public ArrayList<CatalogueItemDto> getCatalogue(MapleType type){
        ArrayList<CatalogueItemDto> result = new ArrayList<>();
        ArrayList<Product> prod = prodrepo.findAllByType(type);

        for(Product p: prod){
            Stock stock = stockrepo.findByProductId(String.valueOf(p.getId()));
            if(stock != null){
                CatalogueItemDto cat = new CatalogueItemDto();
                cat.setId(String.valueOf(p.getId()));
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
