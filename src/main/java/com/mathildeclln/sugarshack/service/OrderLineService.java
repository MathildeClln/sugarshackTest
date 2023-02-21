package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.OrderLineDto;
import com.mathildeclln.sugarshack.dto.OrderValidationResponseDto;
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
        OrderValidationResponseDto result = new OrderValidationResponseDto();
        boolean valid = true;
        ArrayList<String> errors = new ArrayList<>();

        for(OrderLineDto line: orderLines){
            int maxQty;
            maxQty = stockRepository.findByProductId(line.getProductId())
                                .getStock();
            if(line.getQty() > maxQty){
                valid = false;
                errors.add(String.format(
                        "Error for product %s: the quantity asked (%d) is higher than the stock (%d).",
                        line.getProductId(), line.getQty(), maxQty));
            }
        }
        result.setOrderValid(valid);
        result.setErrors(errors);

        return result;
    }
}
