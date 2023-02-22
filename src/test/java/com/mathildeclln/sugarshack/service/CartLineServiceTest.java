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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
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
        given(productRepository.findById(product.getId())).willReturn(Optional.ofNullable(product));
        given(productRepository.findById(product1.getId())).willReturn(Optional.of(product1));

        ArrayList<CartLineDto> cartLines = cartLineService.getCart();

        assertThat(cartLines).isNotNull();
        assertThat(cartLines.size()).isEqualTo(2);

        cartLine1 = cartLines.get(0);
        assertThat(cartLine1.getQty()).isEqualTo(orderLine.getQty());
        assertThat(cartLine1.getName()).isEqualTo(product.getName());
        assertThat(cartLine1.getImage()).isEqualTo(product.getImage());
        assertThat(cartLine1.getPrice()).isEqualTo(product.getPrice());
        assertThat(cartLine1.getProductId()).isEqualTo(product.getId());
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
        given(productRepository.findById(orderLine1.getProductId())).willReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> cartLineService.getCart());
    }

    @Test
    public void addToCartHappyPathTest(){
        // 1. Test to add product not in cart
        String productId = "3";
        OrderLine orderLine2 = new OrderLine(productId, 1);
        Product product2 = new Product(productId, "Maple3",
                                    "", "3.png",
                                        15, MapleType.DARK);

        given(productRepository.existsById(productId)).willReturn(true);
        given(orderLineRepository.existsByProductId(productId)).willReturn(false);

        cartLineService.addToCart(productId);

        given(orderLineRepository.findByProductId(productId)).willReturn(orderLine2);
        given(productRepository.findById(productId)).willReturn(Optional.of(product2));

        cartLine = cartLineService.getCartLineById(productId);

        assertThat(cartLine.getProductId()).isEqualTo(productId);
        assertThat(cartLine.getPrice()).isEqualTo(product2.getPrice());
        assertThat(cartLine.getQty()).isEqualTo(orderLine2.getQty());
        assertThat(cartLine.getName()).isEqualTo(product2.getName());
        assertThat(cartLine.getImage()).isEqualTo(product2.getImage());

        // 2. Test to add product already in cart
        given(orderLineRepository.existsByProductId(productId)).willReturn(true);

        cartLineService.addToCart(productId);

        cartLine = cartLineService.getCartLineById(productId);

        assertThat(cartLine.getProductId()).isEqualTo(productId);
        assertThat(cartLine.getPrice()).isEqualTo(product2.getPrice());
        assertThat(cartLine.getQty()).isEqualTo(orderLine2.getQty());
        assertThat(cartLine.getName()).isEqualTo(product2.getName());
        assertThat(cartLine.getImage()).isEqualTo(product2.getImage());
    }

    @Test
    public void addToCartExceptionTest(){
        given(productRepository.existsById("2")).willReturn(false);

        assertThrows(ProductNotFoundException.class,
                () -> cartLineService.addToCart("2"));
    }

    @Test
    public void removeFromCartHappyPathTest(){
        given(orderLineRepository.existsByProductId(orderLine.getProductId())).willReturn(true);

        cartLineService.removeFromCart(orderLine.getProductId());

        given(orderLineRepository.findByProductId(orderLine.getProductId())).willReturn(null);
        assertThrows(ProductNotFoundException.class,
                () -> cartLine = cartLineService.getCartLineById(orderLine.getProductId()));

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
        given(orderLineRepository.findByProductId(orderLine.getProductId())).willReturn(orderLine);
        given(productRepository.findById(product.getId())).willReturn(Optional.ofNullable(product));

        cartLineService.changeQty(orderLine.getProductId(), 4);
        cartLine = cartLineService.getCartLineById(orderLine.getProductId());

        assertThat(cartLine).isNotNull();
        assertThat(cartLine.getProductId()).isEqualTo(orderLine.getProductId());
        assertThat(cartLine.getQty()).isEqualTo(4);

        cartLineService.changeQty(orderLine.getProductId(), 3);
        cartLine = cartLineService.getCartLineById(orderLine.getProductId());

        assertThat(cartLine).isNotNull();
        assertThat(cartLine.getProductId()).isEqualTo(orderLine.getProductId());
        assertThat(cartLine.getQty()).isEqualTo(3);
    }

    @Test
    public void changeQtyToZeroTest(){
        given(orderLineRepository.findByProductId(orderLine.getProductId())).willReturn(orderLine);
        cartLineService.changeQty(orderLine.getProductId(), 0);

        given(orderLineRepository.findByProductId(orderLine.getProductId())).willReturn(null);
        assertThrows(ProductNotFoundException.class,
                () -> cartLine = cartLineService.getCartLineById(orderLine.getProductId()));

        assertThat(cartLine).isNull();
    }

    @Test
    public void changeQtyInvalidQtyExceptionTest(){
        given(orderLineRepository.findByProductId(orderLine.getProductId())).willReturn(orderLine);
        assertThrows(InvalidQuantityException.class,
                () -> cartLineService.changeQty(orderLine.getProductId(), -1));
    }

    @Test
    public void changeQtyProductNotFoundExceptionTest(){
        given(orderLineRepository.findByProductId("1")).willReturn(null);
        assertThrows(ProductNotFoundException.class,
                () -> cartLineService.changeQty("1", 5));
    }
}
