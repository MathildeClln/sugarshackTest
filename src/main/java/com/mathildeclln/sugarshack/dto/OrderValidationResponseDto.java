package com.mathildeclln.sugarshack.dto;

import java.util.ArrayList;

public class OrderValidationResponseDto {

    private boolean isOrderValid;
    private ArrayList<String> errors;

    public OrderValidationResponseDto() {
    }

    public OrderValidationResponseDto(boolean isOrderValid, ArrayList<String> errors) {
        this.isOrderValid = isOrderValid;
        this.errors = errors;
    }

    public boolean isOrderValid() {
        return isOrderValid;
    }

    public void setOrderValid(boolean orderValid) {
        isOrderValid = orderValid;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }
}
