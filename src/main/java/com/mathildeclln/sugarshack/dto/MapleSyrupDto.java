package com.mathildeclln.sugarshack.dto;

import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.model.Stock;

public class MapleSyrupDto {
    private String id;
    private String name;
    private String description;
    private String image;
    private double price;
    private int stock;
    private MapleType type;

    public MapleSyrupDto() {
    }

    public MapleSyrupDto(Product product, Stock stock){
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.stock = stock.getStock();
        this.type = product.getType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public MapleType getType() {
        return type;
    }

    public void setType(MapleType type) {
        this.type = type;
    }
}
