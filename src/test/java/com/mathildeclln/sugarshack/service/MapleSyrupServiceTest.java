package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.MapleSyrupDto;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.model.Stock;
import com.mathildeclln.sugarshack.repository.ProductRepository;
import com.mathildeclln.sugarshack.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MapleSyrupServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private MapleSyrupService mapleSyrupService;

    private Product product;
    private Stock stock;

    @BeforeEach
    public void initialSteps(){
        productRepository   = Mockito.mock(ProductRepository.class);
        stockRepository     = Mockito.mock(StockRepository.class);
        mapleSyrupService   = new MapleSyrupService(productRepository, stockRepository);

        product             = new Product("1", "Maple1", "...",
                                    "image1.jpg", 10.5, MapleType.AMBER);
        stock               = new Stock("1", 20);
    }

    @Test
    public void getInfoHappyPathTest(){
        MapleSyrupDto mapleSyrup;

        given(productRepository.findById(product.getId())).willReturn(Optional.ofNullable(product));
        given(stockRepository.findByProductId(stock.getProductId())).willReturn(stock);

        mapleSyrup = mapleSyrupService.getInfo(product.getId());

        assertThat(mapleSyrup).isNotNull();
        assertThat(mapleSyrup.getId()).isEqualTo(product.getId());
        assertThat(mapleSyrup.getName()).isEqualTo(product.getName());
        assertThat(mapleSyrup.getDescription()).isEqualTo(product.getDescription());
        assertThat(mapleSyrup.getImage()).isEqualTo(product.getImage());
        assertThat(mapleSyrup.getPrice()).isEqualTo(product.getPrice());
        assertThat(mapleSyrup.getType()).isEqualTo(product.getType());
        assertThat(mapleSyrup.getStock()).isEqualTo(stock.getStock());
    }

    @Test
    public void getInfoExceptionTest(){
        given(productRepository.findById("4")).willReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> mapleSyrupService.getInfo("4"));
    }
}
