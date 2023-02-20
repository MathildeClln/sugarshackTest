package com.mathildeclln.sugarshack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@AllArgsConstructor
@Builder
public class OrderLine {
    @Id
    private String productId;
    private int qty;

    public OrderLine() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

}
