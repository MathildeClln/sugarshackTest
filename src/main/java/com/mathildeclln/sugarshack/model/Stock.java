package com.mathildeclln.sugarshack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Stock {
    @Id
    private String productId;
    private int stock;

    public Stock(){
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
