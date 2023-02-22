package com.mathildeclln.sugarshack.service;


import com.mathildeclln.sugarshack.dto.CartLineDto;
import com.mathildeclln.sugarshack.exception.InvalidQuantityException;
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

public class CartLineServiceTest{

    @Mock
    private OrderLineRepository orderLineRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private CartLineService cartLineService;

    private OrderLine orderLine;
    private Product product;
    private CartLineDto cartLine;

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
        OrderLine   orderLine1 = new OrderLine("2", 1);
        Product     product1 = new Product("2", "", "", "", 10, MapleType.CLEAR);
        CartLineDto cartLine1;

        given(orderLineRepository.findAll()).willReturn(List.of(orderLine, orderLine1));
        given(productRepository.findById("1")).willReturn(Optional.ofNullable(product));
        given(productRepository.findById("2")).willReturn(Optional.of(product1));

        ArrayList<CartLineDto> cartLines = cartLineService.getCart();

        assertThat(cartLines).isNotNull();
        assertThat(cartLines.size()).isEqualTo(2);

        cartLine1 = cartLines.get(0);
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

        given(orderLineRepository.findAll()).willReturn(List.of(orderLine1));
        given(productRepository.findById("1")).willReturn(Optional.empty());

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

        given(productRepository.existsById("3")).willReturn(true);
        given(orderLineRepository.existsByProductId("3")).willReturn(false);

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
        given(orderLineRepository.existsByProductId("3")).willReturn(true);

        cartLineService.addToCart("3");

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
    public void removeFromCartHappyPathTest(){
        given(orderLineRepository.existsByProductId("1")).willReturn(true);

        cartLineService.removeFromCart("1");

        given(orderLineRepository.findByProductId("1")).willReturn(null);
        assertThrows(ProductNotFoundException.class,
                () -> cartLine = cartLineService.getCartLineById("1"));

        assertThat(cartLine).isNull();
    }

    @Test
    public void removeFromCartExceptionTest(){
        given(orderLineRepository.existsByProductId("1")).willReturn(false);

        assertThrows(ProductNotFoundException.class,
                () -> cartLineService.removeFromCart("1"));
    }

    @Test
    public void changeQtyHappyPathTest(){
        given(orderLineRepository.findByProductId("1")).willReturn(orderLine);
        given(productRepository.findById("1")).willReturn(Optional.ofNullable(product));

        cartLineService.changeQty("1", 4);
        cartLine = cartLineService.getCartLineById("1");

        assertThat(cartLine).isNotNull();
        assertThat(cartLine.getProductId()).isEqualTo("1");
        assertThat(cartLine.getQty()).isEqualTo(4);

        cartLineService.changeQty("1", 3);
        cartLine = cartLineService.getCartLineById("1");

        assertThat(cartLine).isNotNull();
        assertThat(cartLine.getProductId()).isEqualTo("1");
        assertThat(cartLine.getQty()).isEqualTo(3);
    }

    @Test
    public void changeQtyToZeroTest(){
        given(orderLineRepository.findByProductId("1")).willReturn(orderLine);
        cartLineService.changeQty("1", 0);

        given(orderLineRepository.findByProductId("1")).willReturn(null);
        assertThrows(ProductNotFoundException.class,
                () -> cartLine = cartLineService.getCartLineById("1"));

        assertThat(cartLine).isNull();
    }

    @Test
    public void changeQtyInvalidQtyExceptionTest(){
        given(orderLineRepository.findByProductId("1")).willReturn(orderLine);
        assertThrows(InvalidQuantityException.class,
                () -> cartLineService.changeQty("1", -1));
    }

    @Test
    public void changeQtyProductNotFoundExceptionTest(){
        given(orderLineRepository.findByProductId("1")).willReturn(null);
        assertThrows(ProductNotFoundException.class,
                () -> cartLineService.changeQty("1", 5));
    }
}
