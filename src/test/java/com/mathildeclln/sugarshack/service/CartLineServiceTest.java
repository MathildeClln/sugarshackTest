package com.mathildeclln.sugarshack.service;


import com.mathildeclln.sugarshack.dto.CartLineDto;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.model.OrderLine;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.repository.OrderLineRepository;
import com.mathildeclln.sugarshack.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;

public class CartLineServiceTest{

    @Mock
    private OrderLineRepository orderLineRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private CartLineService cartLineService;

    private OrderLine orderLine;
    private Product product;

    @BeforeEach
    public void initialSteps(){
        orderLineRepository = Mockito.mock(OrderLineRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        cartLineService = new CartLineService(orderLineRepository, productRepository);

        orderLine = new OrderLine("1", 3);
        product = new Product("1", "Maple1",
                        "Our best", "m1.jpg",
                            12, MapleType.AMBER);
    }


    @Test
    public void getCartHappyPathTest() {
        OrderLine orderLine1 = new OrderLine("2", 1);
        Product product1 = new Product("2", "", "", "", 10, MapleType.CLEAR);

        given(orderLineRepository.findAll()).willReturn(List.of(orderLine, orderLine1));
        given(productRepository.findById("1")).willReturn(Optional.ofNullable(product));
        given(productRepository.findById("2")).willReturn(Optional.of(product1));

        ArrayList<CartLineDto> cartLines = cartLineService.getCart();

        assertThat(cartLines).isNotNull();
        assertThat(cartLines.size()).isEqualTo(2);

        CartLineDto cartLine1 = cartLines.get(0);
        assertThat(cartLine1.getQty()).isEqualTo(3);
        assertThat(cartLine1.getName()).isEqualTo("Maple1");
        assertThat(cartLine1.getImage()).isEqualTo("m1.jpg");
        assertThat(cartLine1.getPrice()).isEqualTo(12);
        assertThat(cartLine1.getProductId()).isEqualTo("1");
    }
    @Test
    public void getCartEmptyTest(){
        ArrayList<CartLineDto> cartLines;

        given(orderLineRepository.findAll()).willReturn(Collections.emptyList());

        cartLines = cartLineService.getCart();

        assertThat(cartLines).isNotNull();
        assertThat(cartLines.isEmpty()).isTrue();

    }

    @Test
    public void getCartExceptionTest(){
        OrderLine orderLine1 = new OrderLine("2", 1);

        given(orderLineRepository.findAll()).willReturn(List.of(orderLine, orderLine1));
        given(productRepository.findById("1")).willReturn(Optional.ofNullable(product));

        assertThrows(ProductNotFoundException.class,
                () -> cartLineService.getCart());
    }

    @Test
    public void addToCartHappyPathTest(){
        // 1. Test to add product not in cart
        OrderLine orderLine2 = new OrderLine("3", 1);
        Product product2 = new Product("3", "Maple3",
                                    "", "3.png",
                                        15, MapleType.DARK);
        CartLineDto cartLine;

        given(productRepository.existsById("3")).willReturn(true);
        given(orderLineRepository.existsByProductId("3")).willReturn(false);
        given(orderLineRepository.save(orderLine2)).willReturn(orderLine2);

        cartLineService.addToCart("3");

        given(orderLineRepository.findByProductId("3")).willReturn(orderLine2);
        given(productRepository.findById("3")).willReturn(Optional.of(product2));

        cartLine = cartLineService.getCartLineById("3");

        assertThat(cartLine.getProductId()).isEqualTo("3");
        assertThat(cartLine.getPrice()).isEqualTo(15);
        assertThat(cartLine.getQty()).isEqualTo(1);
        assertThat(cartLine.getName()).isEqualTo("Maple3");
        assertThat(cartLine.getImage()).isEqualTo("3.png");

        // 2. Test to add product already in cart
        given(productRepository.existsById("3")).willReturn(true);
        given(orderLineRepository.existsByProductId("3")).willReturn(true);
        given(orderLineRepository.save(orderLine2)).willReturn(orderLine2);

        cartLineService.addToCart("3");

        given(orderLineRepository.findByProductId("3")).willReturn(orderLine2);
        given(productRepository.findById("3")).willReturn(Optional.of(product2));

        cartLine = cartLineService.getCartLineById("3");

        assertThat(cartLine.getProductId()).isEqualTo("3");
        assertThat(cartLine.getPrice()).isEqualTo(15);
        assertThat(cartLine.getQty()).isEqualTo(2);
        assertThat(cartLine.getName()).isEqualTo("Maple3");
        assertThat(cartLine.getImage()).isEqualTo("3.png");
    }

    @Test
    public void addToCartExceptionTest(){
        given(productRepository.existsById("2")).willReturn(false);

        assertThrows(ProductNotFoundException.class,
                () -> cartLineService.addToCart("2"));
    }

    @Test
    public void removeFromCartTest(){
        given(orderLineRepository.existsByProductId("1")).willReturn(true);
        willDoNothing().given(orderLineRepository).deleteByProductId("1");

        cartLineService.removeFromCart("1");

        given(orderLineRepository.findByProductId("1")).willReturn(null);
        given(productRepository.findById("1")).willReturn(Optional.ofNullable(product));

        CartLineDto cartLine = cartLineService.getCartLineById("1");

        assertThat(cartLine).isNull();
    }

    @Test
    public void changeQtyTest(){

        given(orderLineRepository.findByProductId("1")).willReturn(orderLine);
        given(orderLineRepository.save(orderLine)).willReturn(orderLine);

        cartLineService.changeQty("1", 4);

        given(orderLineRepository.findByProductId("1")).willReturn(orderLine);
        given(productRepository.findById("1")).willReturn(Optional.ofNullable(product));

        CartLineDto cartLine = cartLineService.getCartLineById("1");

        assertThat(cartLine.getQty()).isEqualTo(4);
    }
}
