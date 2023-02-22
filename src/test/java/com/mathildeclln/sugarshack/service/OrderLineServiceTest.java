package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.OrderLineDto;
import com.mathildeclln.sugarshack.dto.OrderValidationResponseDto;
import com.mathildeclln.sugarshack.exception.InvalidQuantityException;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.Stock;
import com.mathildeclln.sugarshack.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrderLineServiceTest {
    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private OrderLineService orderLineService;

    private ArrayList<OrderLineDto> orderLines;
    private Stock stock;
    private OrderLineDto orderLine1;
    private OrderValidationResponseDto orderValidationResponse;

    @BeforeEach
    public void initialSteps(){
        stockRepository = Mockito.mock(StockRepository.class);
        orderLineService = new OrderLineService(stockRepository);

        orderLine1 = new OrderLineDto("1", 1);
        orderLines = new ArrayList<>();
        orderLines.add(orderLine1);
        stock = new Stock("1", 10);
    }

    @Test
    public void placeOrderValidTest(){
        given(stockRepository.findByProductId("1")).willReturn(stock);

        orderValidationResponse = orderLineService.placeOrder(orderLines);

        assertThat(orderValidationResponse).isNotNull();
        assertThat(orderValidationResponse.isOrderValid()).isTrue();
        assertThat(orderValidationResponse.getErrors().isEmpty()).isTrue();
    }

    @Test
    public void placeOrderInvalidTest(){
        OrderLineDto    orderLineInvalid = new OrderLineDto("2", 10);
        Stock           stockInvalid = new Stock("2", 5);

        orderLines.add(orderLineInvalid);
        given(stockRepository.findByProductId("1")).willReturn(stock);
        given(stockRepository.findByProductId("2")).willReturn(stockInvalid);

        orderValidationResponse = orderLineService.placeOrder(orderLines);

        assertThat(orderValidationResponse).isNotNull();
        assertThat(orderValidationResponse.isOrderValid()).isFalse();
        assertThat(orderValidationResponse.getErrors().size()).isEqualTo(1);

        orderLine1.setQty(11);
        orderValidationResponse = orderLineService.placeOrder(orderLines);

        assertThat(orderValidationResponse).isNotNull();
        assertThat(orderValidationResponse.isOrderValid()).isFalse();
        assertThat(orderValidationResponse.getErrors().size()).isEqualTo(2);
    }

    @Test
    public void placeOrderProductNotFoundExceptionTest(){
        OrderLineDto    orderLineException = new OrderLineDto("3", 2);

        orderLines.add(orderLineException);
        given(stockRepository.findByProductId("3")).willReturn(null);

        assertThrows(ProductNotFoundException.class,
                () -> orderValidationResponse = orderLineService.placeOrder(orderLines));

        assertThat(orderValidationResponse).isNull();
    }

    @Test
    public void placeOrderInvalidQuantityExceptionTest(){
        OrderLineDto    orderLineException = new OrderLineDto("2", -1);
        Stock           stock2 = new Stock("2", 10);

        orderLines.add(orderLineException);
        given(stockRepository.findByProductId("1")).willReturn(stock);
        given(stockRepository.findByProductId("2")).willReturn(stock2);

        assertThrows(InvalidQuantityException.class,
                () -> orderValidationResponse = orderLineService.placeOrder(orderLines));

        assertThat(orderValidationResponse).isNull();
    }
}
