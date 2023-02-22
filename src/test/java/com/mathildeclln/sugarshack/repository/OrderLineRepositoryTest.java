package com.mathildeclln.sugarshack.repository;

import com.mathildeclln.sugarshack.model.OrderLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderLineRepositoryTest {
    @Autowired
    private OrderLineRepository orderLineRepository;
    private OrderLine orderLine;

    @BeforeEach
    public void initialSteps(){
        orderLine = new OrderLine("1", 2);
    }

    @Test
    public void saveTest(){
        OrderLine orderLine1;

        orderLine1 = orderLineRepository.save(orderLine);

        assertThat(orderLineRepository.existsByProductId(orderLine.getProductId())).isTrue();
        assertThat(orderLine1).isNotNull();
        assertThat(orderLine1.getProductId()).isEqualTo(orderLine.getProductId());
        assertThat(orderLine1.getQty()).isEqualTo(orderLine.getQty());

    }
    @Test
    public void findByProductIdTest(){
        OrderLine orderLine1;

        orderLineRepository.save(orderLine);
        orderLine1 = orderLineRepository.findByProductId(orderLine.getProductId());

        assertThat(orderLine1).isNotNull();
        assertThat(orderLine1.getProductId()).isEqualTo(orderLine.getProductId());
        assertThat(orderLine1.getQty()).isEqualTo(orderLine.getQty());
    }

    @Test
    public void existsByProductIdTest(){
        orderLineRepository.save(orderLine);

        assertThat(orderLineRepository.existsByProductId(orderLine.getProductId())).isTrue();
    }

    @Test
    public void deleteByProductIdTest(){
        orderLineRepository.save(orderLine);
        assertThat(orderLineRepository.existsByProductId(orderLine.getProductId())).isTrue();

        orderLineRepository.deleteByProductId(orderLine.getProductId());

        assertThat(orderLineRepository.existsByProductId(orderLine.getProductId())).isFalse();
    }

    @Test
    public void findAllTest(){
        List<OrderLine> orderLines;

        OrderLine orderLine1 = new OrderLine("2", 3);
        orderLineRepository.save(orderLine);
        orderLineRepository.save(orderLine1);

        orderLines = orderLineRepository.findAll();

        assertThat(orderLines).isNotNull();
        assertThat(orderLines.size()).isEqualTo(2);
        assertThat(orderLines.get(0).getProductId()).isEqualTo(orderLine.getProductId());
        assertThat(orderLines.get(1).getProductId()).isEqualTo(orderLine1.getProductId());
    }

}
