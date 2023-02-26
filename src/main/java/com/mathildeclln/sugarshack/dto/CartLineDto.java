package com.mathildeclln.sugarshack.dto;

import com.mathildeclln.sugarshack.model.OrderLine;
import com.mathildeclln.sugarshack.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class CartLineDto {

    private String productId;
    private String name;
    private String image;
    private Double price;
    private Integer qty;

    public CartLineDto(Product product, OrderLine orderLine){
        this.productId = product.getId();
        this.name = product.getName();
        this.image = product.getImage();
        this.price = product.getPrice();
        this.qty = orderLine.getQty();
    }
}
