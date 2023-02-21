package com.mathildeclln.sugarshack.dto;

import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.model.Stock;

public class CatalogueItemDto {
    private String id;
    private String name;
    private String image;
    private double price;
    private int maxQty;
    private MapleType type;

    public CatalogueItemDto() {
    }

    public CatalogueItemDto(Product product, Stock stock){
        this.id = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.type = product.getType();
        this.maxQty = stock.getStock();
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

    public int getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
    }

    public MapleType getType() {
        return type;
    }

    public void setType(MapleType type) {
        this.type = type;
    }
}
