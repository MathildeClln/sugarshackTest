package com.mathildeclln.sugarshack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class OrderLineDto {
    private String productId;
    private int qty;
}
