package com.mathildeclln.sugarshack.controller;

import com.mathildeclln.sugarshack.dto.OrderValidationResponseDto;
import com.mathildeclln.sugarshack.model.OrderLine;
import com.mathildeclln.sugarshack.service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/order")
public class OrderLineController {
    @Autowired
    private OrderLineService orderLineService;

    @PostMapping
    public OrderValidationResponseDto placeOrder(@RequestBody ArrayList<OrderLine> lines){
        return orderLineService.placeOrder(lines);
    }
}
