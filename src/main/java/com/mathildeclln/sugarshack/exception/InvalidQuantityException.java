package com.mathildeclln.sugarshack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidQuantityException extends RuntimeException{

    public InvalidQuantityException(int quantity){
        super(String.format("The quantity %d is not valid.", quantity));
    }
}
