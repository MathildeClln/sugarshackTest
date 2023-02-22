package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.OrderLineDto;
import com.mathildeclln.sugarshack.dto.OrderValidationResponseDto;
import com.mathildeclln.sugarshack.exception.InvalidQuantityException;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.Stock;
import com.mathildeclln.sugarshack.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderLineService {
    @Autowired
    private StockRepository stockRepository;

    public OrderLineService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public OrderValidationResponseDto placeOrder(ArrayList<OrderLineDto> orderLines){
        boolean                     valid = true;
        ArrayList<String>           errors = new ArrayList<>();
        Stock                       stock;
        int                         maxQuantity;

        for(OrderLineDto line: orderLines){
            stock = stockRepository.findByProductId(line.getProductId());

            if(stock == null){
                throw new ProductNotFoundException(line.getProductId());
            }
            else {
                maxQuantity = stock.getStock();
                if(line.getQty() <= 0){
                    throw new InvalidQuantityException(line.getQty());
                }
                else if (line.getQty() > maxQuantity) {
                    valid = false;
                    errors.add(String.format(
                            "Error for product %s: the quantity asked (%d) is higher than the stock (%d).",
                            line.getProductId(), line.getQty(), maxQuantity));
                }
            }
        }
        return new OrderValidationResponseDto(valid, errors);
    }
}
