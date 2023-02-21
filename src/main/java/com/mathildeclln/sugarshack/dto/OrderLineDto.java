package com.mathildeclln.sugarshack.dto;

public class OrderLineDto {
    private String productId;
    private int qty;

    public OrderLineDto() {
    }

    public OrderLineDto(String productId, int qty) {
        this.productId = productId;
        this.qty = qty;
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
