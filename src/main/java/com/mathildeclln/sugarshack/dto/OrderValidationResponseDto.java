package com.mathildeclln.sugarshack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter @AllArgsConstructor
public class OrderValidationResponseDto {

    private boolean isOrderValid;
    private ArrayList<String> errors;
}
