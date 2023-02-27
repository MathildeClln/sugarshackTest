package com.mathildeclln.sugarshack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathildeclln.sugarshack.dto.OrderLineDto;
import com.mathildeclln.sugarshack.dto.OrderValidationResponseDto;
import com.mathildeclln.sugarshack.exception.InvalidQuantityException;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void placeOrderValidTest() throws Exception {
        OrderLineDto orderLineValid = new OrderLineDto("1", 1);
        ArrayList<OrderLineDto> orderLines = new ArrayList<>();
        orderLines.add(orderLineValid);
        OrderValidationResponseDto orderValidationResponse =
                new OrderValidationResponseDto(true, new ArrayList<>());

        given(orderService.placeOrder(any())).willReturn(orderValidationResponse);

        ResultActions result = mockMvc.perform(post("/order")
                .content(asJsonString(orderLines))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk()).andDo(print());
        result.andExpect(jsonPath("$.orderValid").value(orderValidationResponse.isOrderValid()));
        result.andExpect(jsonPath("$.errors").isArray());
        result.andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    public void placeOrderInvalidTest() throws Exception{
        OrderLineDto orderLineValid = new OrderLineDto("1", 1);
        OrderLineDto orderLineInvalid1 = new OrderLineDto("2", 40);
        OrderLineDto orderLineInvalid2 = new OrderLineDto("3", 50);
        ArrayList<OrderLineDto> orderLines = new ArrayList<>();
        orderLines.add(orderLineValid);
        orderLines.add(orderLineInvalid1);
        orderLines.add(orderLineInvalid2);
        ArrayList<String> errors = new ArrayList<>();
        errors.add("Error for product 2 the quantity asked (40) is higher than the stock (30).");
        errors.add("Error for product 3 the quantity asked (50) is higher than the stock (20).");

        OrderValidationResponseDto orderValidationResponse =
                new OrderValidationResponseDto(false, errors);

        given(orderService.placeOrder(any())).willReturn(orderValidationResponse);

        ResultActions result = mockMvc.perform(post("/order")
                .content(asJsonString(orderLines))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk()).andDo(print());
        result.andExpect(jsonPath("$.orderValid").value(orderValidationResponse.isOrderValid()));
        result.andExpect(jsonPath("$.errors").isArray());
        result.andExpect(jsonPath("$.errors.length()").value(2));
    }

    @Test
    public void placeOrderInvalidExceptionTest() throws Exception {
        OrderLineDto orderLineValid = new OrderLineDto("1", -1);
        ArrayList<OrderLineDto> orderLines = new ArrayList<>();
        orderLines.add(orderLineValid);

        given(orderService.placeOrder(any())).willThrow(InvalidQuantityException.class);

        ResultActions result = mockMvc.perform(post("/order")
                .content(asJsonString(orderLines))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void placeOrderNotFoundExceptionTest() throws Exception {
        OrderLineDto orderLineValid = new OrderLineDto("4", 2);
        ArrayList<OrderLineDto> orderLines = new ArrayList<>();
        orderLines.add(orderLineValid);

        given(orderService.placeOrder(any())).willThrow(ProductNotFoundException.class);

        ResultActions result = mockMvc.perform(post("/order")
                .content(asJsonString(orderLines))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound()).andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
