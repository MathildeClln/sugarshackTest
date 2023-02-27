package com.mathildeclln.sugarshack.controller;

import com.mathildeclln.sugarshack.dto.CartLineDto;
import com.mathildeclln.sugarshack.exception.InvalidQuantityException;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    public void getCartTest() throws Exception {
        ArrayList<CartLineDto> cartLines = new ArrayList<>();
        CartLineDto cartLine1 = new CartLineDto("1", "Maple1",
                                                "img1.jpg", 10.33,2);
        CartLineDto cartLine2 = new CartLineDto("2", "Maple2",
                "img2.jpg", 13.45,3);
        cartLines.add(cartLine1);
        cartLines.add(cartLine2);

        given(cartService.getCart()).willReturn(cartLines);

        ResultActions result = mockMvc.perform(get("/cart"));

        result.andExpect(status().isOk()).andDo(print());
        result.andExpect(jsonPath("$.length()").value(2));

        result.andExpect(jsonPath("$[0].productId").value(cartLine1.getProductId()));
        result.andExpect(jsonPath("$[0].name").value(cartLine1.getName()));
        result.andExpect(jsonPath("$[0].image").value(cartLine1.getImage()));
        result.andExpect(jsonPath("$[0].price").value(cartLine1.getPrice()));
        result.andExpect(jsonPath("$[0].qty").value(cartLine1.getQty()));

        result.andExpect(jsonPath("$[1].productId").value(cartLine2.getProductId()));
        result.andExpect(jsonPath("$[1].name").value(cartLine2.getName()));
        result.andExpect(jsonPath("$[1].image").value(cartLine2.getImage()));
        result.andExpect(jsonPath("$[1].price").value(cartLine2.getPrice()));
        result.andExpect(jsonPath("$[1].qty").value(cartLine2.getQty()));
    }

    @Test
    public void getCartExceptionTest() throws Exception{

        given(cartService.getCart()).willThrow(ProductNotFoundException.class);

        ResultActions result = mockMvc.perform(get("/cart"));

        result.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void addToCartTest() throws Exception{

        willDoNothing().given(cartService).addToCart("1");

        ResultActions result = mockMvc.perform(put("/cart").param("productId", "1"));

        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void addToCartExceptionTest() throws Exception{
        willThrow(ProductNotFoundException.class).given(cartService).addToCart("4");

        ResultActions result = mockMvc.perform(put("/cart").param("productId", "4"));

        result.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void removeFromCartTest() throws Exception{
        willDoNothing().given(cartService).removeFromCart("1");

        ResultActions result = mockMvc.perform(delete("/cart")
                .param("productId", "1"));

        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void removeFromCartExceptionTest() throws Exception{
        willThrow(ProductNotFoundException.class).given(cartService).removeFromCart("4");

        ResultActions result = mockMvc.perform(delete("/cart").param("productId", "4"));

        result.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void changeQtyTest() throws Exception{
        willDoNothing().given(cartService).changeQty("1", 2);

        ResultActions result = mockMvc.perform(patch("/cart")
                .param("productId", "1")
                .param("newQty", "2"));

        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void changeQtyInvalidException() throws Exception{
        willThrow(InvalidQuantityException.class).given(cartService).changeQty("1", -1);

        ResultActions result = mockMvc.perform(patch("/cart")
                .param("productId", "1")
                .param("newQty", "-1"));

        result.andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void changeQtyNotFoundException() throws Exception{
        willThrow(ProductNotFoundException.class).given(cartService).changeQty("4", 2);

        ResultActions result = mockMvc.perform(patch("/cart")
                .param("productId", "4")
                .param("newQty", "2"));

        result.andExpect(status().isNotFound()).andDo(print());
    }
}
