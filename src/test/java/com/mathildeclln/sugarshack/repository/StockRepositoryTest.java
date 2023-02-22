package com.mathildeclln.sugarshack.repository;

import com.mathildeclln.sugarshack.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StockRepositoryTest {
    @Autowired
    private StockRepository stockRepository;

    private Stock stock;

    @BeforeEach
    public void initialSteps(){
        stock = new Stock("1", 20);
    }

    @Test
    public void saveTest(){
        Stock stockSaved = stockRepository.save(stock);

        assertThat(stockSaved).isNotNull();
        assertThat(stockSaved.getProductId()).isEqualTo(stock.getProductId());
        assertThat(stockSaved.getStock()).isEqualTo(stock.getStock());
    }

    @Test
    public void findByProductIdTest(){
        stockRepository.save(stock);

        Stock stockFound = stockRepository.findByProductId(stock.getProductId());

        assertThat(stockFound).isNotNull();
        assertThat(stockFound.getProductId()).isEqualTo(stock.getProductId());
        assertThat(stockFound.getStock()).isEqualTo(stock.getStock());
    }
}
