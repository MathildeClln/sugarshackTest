package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.OrderValidationResponseDto;
import com.mathildeclln.sugarshack.model.OrderLine;
import com.mathildeclln.sugarshack.repository.OrderLineRepository;
import com.mathildeclln.sugarshack.repository.ProductRepository;
import com.mathildeclln.sugarshack.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OrderLineService {
    @Autowired
    private StockRepository stockRepo;

    public OrderValidationResponseDto placeOrder(ArrayList<OrderLine> orderLines){
        OrderValidationResponseDto result = new OrderValidationResponseDto();
        boolean valid = true;
        ArrayList<String> errors = new ArrayList<>();

        for(OrderLine line: orderLines){
            int maxQty;
            maxQty = stockRepo.findByProductId(line.getProductId())
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
