package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.MapleSyrupDto;
import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.model.Product;
import com.mathildeclln.sugarshack.model.Stock;
import com.mathildeclln.sugarshack.repository.ProductRepository;
import com.mathildeclln.sugarshack.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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
        productRepository = Mockito.mock(ProductRepository.class);
        stockRepository = Mockito.mock(StockRepository.class);
        mapleSyrupService = new MapleSyrupService(productRepository, stockRepository);

        product = new Product("1", "Maple1", "...",
                            "image1.jpg", 10.5, MapleType.AMBER);
        stock = new Stock("1", 20);
    }

    @Test
    public void getInfoTest(){
        MapleSyrupDto mapleSyrup;

        given(productRepository.findById("1")).willReturn(Optional.ofNullable(product));
        given(stockRepository.findByProductId("1")).willReturn(stock);

        mapleSyrup = mapleSyrupService.getInfo("1");

        assertThat(mapleSyrup).isNotNull();
        assertThat(mapleSyrup.getId()).isEqualTo("1");
        assertThat(mapleSyrup.getName()).isEqualTo("Maple1");
        assertThat(mapleSyrup.getDescription()).isEqualTo("...");
        assertThat(mapleSyrup.getImage()).isEqualTo("image1.jpg");
        assertThat(mapleSyrup.getPrice()).isEqualTo(10.5);
        assertThat(mapleSyrup.getType()).isEqualTo(MapleType.AMBER);
        assertThat(mapleSyrup.getStock()).isEqualTo(20);
    }
}
