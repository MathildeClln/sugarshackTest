package com.mathildeclln.sugarshack.dto;

import com.mathildeclln.sugarshack.model.OrderLine;
import com.mathildeclln.sugarshack.model.Product;

public class CartLineDto {

    private String productId;
    private String name;
    private String image;
    private Double price;
    private Integer qty;

    public CartLineDto(){

    }

    public CartLineDto(Product product, OrderLine orderLine){
        this.productId = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.qty = orderLine.getQty();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
