package com.mathildeclln.sugarshack.dto;

import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class CatalogueItemDto {
    private String id;
    private String name;
    private String image;
    private double price;
    private int maxQty;
    private MapleType type;

    public CatalogueItemDto(Product product, Stock stock){
        this.id = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.type = product.getType();
        this.maxQty = stock.getStock();
    }
}
