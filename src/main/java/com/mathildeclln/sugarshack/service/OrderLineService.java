package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.OrderLineDto;
import com.mathildeclln.sugarshack.dto.OrderValidationResponseDto;
import com.mathildeclln.sugarshack.exception.InvalidQuantityException;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.Stock;
import com.mathildeclln.sugarshack.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class OrderLineService {
    @Autowired
    private StockRepository stockRepository;

    public OrderValidationResponseDto placeOrder(ArrayList<OrderLineDto> orderLines){
        boolean                     valid = true;
        ArrayList<String>           errors = new ArrayList<>();
        Stock                       stock;
        int                         maxQuantity;

        for(OrderLineDto orderLine: orderLines){
            stock = stockRepository.findByProductId(orderLine.getProductId());

            if(stock == null){
                throw new ProductNotFoundException(orderLine.getProductId());
            }
            else {
                maxQuantity = stock.getStock();
                if(orderLine.getQty() <= 0){
                    throw new InvalidQuantityException(orderLine.getQty());
                }
                else if (orderLine.getQty() > maxQuantity) {
                    valid = false;
                    errors.add(String.format(
                            "Error for product %s: the quantity asked (%d) is higher than the stock (%d).",
                            orderLine.getProductId(), orderLine.getQty(), maxQuantity));
                }
            }
        }
        return new OrderValidationResponseDto(valid, errors);
    }
}
