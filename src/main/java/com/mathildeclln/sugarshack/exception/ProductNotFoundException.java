package com.mathildeclln.sugarshack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException{
    private final String productId;
    public ProductNotFoundException(String productId){
        super(String.format("Product %s not found.", productId));
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
