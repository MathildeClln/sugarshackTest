package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.OrderLineDto;
import com.mathildeclln.sugarshack.dto.OrderValidationResponseDto;
import com.mathildeclln.sugarshack.exception.InvalidQuantityException;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.Stock;
import com.mathildeclln.sugarshack.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private OrderService orderService;

    private ArrayList<OrderLineDto> orderLines;
    private Stock stock;
    private OrderLineDto orderLine1;
    private OrderValidationResponseDto orderValidationResponse;

    @BeforeEach
    public void initialSteps(){
        stockRepository = Mockito.mock(StockRepository.class);
        orderService = new OrderService(stockRepository);

        orderLine1 = new OrderLineDto("1", 1);
        orderLines = new ArrayList<>();
        orderLines.add(orderLine1);
        stock = new Stock("1", 10);
    }

    @Test
    public void placeOrderValidTest(){
        given(stockRepository.findByProductId(orderLine1.getProductId())).willReturn(stock);

        orderValidationResponse = orderService.placeOrder(orderLines);

        assertThat(orderValidationResponse).isNotNull();
        assertThat(orderValidationResponse.isOrderValid()).isTrue();
        assertThat(orderValidationResponse.getErrors().isEmpty()).isTrue();
    }

    @Test
    public void placeOrderInvalidTest(){
        OrderLineDto    orderLineInvalid = new OrderLineDto("2", 10);
        Stock           stockInvalid = new Stock("2", 5);

        orderLines.add(orderLineInvalid);
        given(stockRepository.findByProductId(stock.getProductId())).willReturn(stock);
        given(stockRepository.findByProductId(stockInvalid.getProductId())).willReturn(stockInvalid);

        orderValidationResponse = orderService.placeOrder(orderLines);

        assertThat(orderValidationResponse).isNotNull();
        assertThat(orderValidationResponse.isOrderValid()).isFalse();
        assertThat(orderValidationResponse.getErrors().size()).isEqualTo(1);

        orderLine1.setQty(stock.getStock()+1);
        orderValidationResponse = orderService.placeOrder(orderLines);

        assertThat(orderValidationResponse).isNotNull();
        assertThat(orderValidationResponse.isOrderValid()).isFalse();
        assertThat(orderValidationResponse.getErrors().size()).isEqualTo(2);
    }

    @Test
    public void placeOrderProductNotFoundExceptionTest(){
        OrderLineDto    orderLineException = new OrderLineDto("3", 2);

        orderLines.add(orderLineException);
        given(stockRepository.findByProductId(stock.getProductId())).willReturn(stock);
        given(stockRepository.findByProductId(orderLineException.getProductId())).willReturn(null);

        assertThrows(ProductNotFoundException.class,
                () -> orderValidationResponse = orderService.placeOrder(orderLines));

        assertThat(orderValidationResponse).isNull();
    }

    @Test
    public void placeOrderInvalidQuantityExceptionTest(){
        OrderLineDto    orderLineException = new OrderLineDto("2", -1);
        Stock           stock2 = new Stock("2", 10);

        orderLines.add(orderLineException);
        given(stockRepository.findByProductId(stock.getProductId())).willReturn(stock);
        given(stockRepository.findByProductId(stock2.getProductId())).willReturn(stock2);

        assertThrows(InvalidQuantityException.class,
                () -> orderValidationResponse = orderService.placeOrder(orderLines));

        assertThat(orderValidationResponse).isNull();
    }
}
