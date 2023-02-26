package com.mathildeclln.sugarshack.dto;

import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.model.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class MapleSyrupDto {
    private String id;
    private String name;
    private String description;
    private String image;
    private double price;
    private int stock;
    private MapleType type;

    public MapleSyrupDto(Product product, Stock stock){
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.stock = stock.getStock();
        this.type = product.getType();
    }
}
