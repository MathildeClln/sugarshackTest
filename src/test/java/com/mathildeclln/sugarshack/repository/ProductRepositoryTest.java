package com.mathildeclln.sugarshack.repository;

import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void initialSteps(){
        product = new Product("1", "Maple 1", "Our best maple syrup !",
                            "~/img/maple1.jpg", 15.75, MapleType.AMBER);
    }

    @Test
    public void saveTest(){
        Product product1 = productRepository.save(product);

        assertThat(productRepository.existsById(product.getId())).isTrue();
        assertThat(product1).isNotNull();
        assertThat(product1).isEqualTo(product);
    }

    @Test
    public void findByIdTest(){
        productRepository.save(product);
        Optional<Product> product1 = productRepository.findById(product.getId());

        assertThat(product1).isNotNull();
        assertThat(product1.isEmpty()).isFalse();
        assertThat(product1.get()).isEqualTo(product);

        product1 = productRepository.findById("2");

        assertThat(product1).isNotNull();
        assertThat(product1.isEmpty()).isTrue();
    }

    @Test
    public void existsByIdTest(){
        productRepository.save(product);

        assertThat(productRepository.existsById(product.getId())).isTrue();
    }

    @Test
    public void findAllByTypeTest(){
        Product productAmber = new Product("2", "Maple 2",
                                    "500 mL format", "~/img/maple2.jpg",
                                        18.99, MapleType.AMBER);
        Product productDark = new Product("3", "Maple 3",
                                            "Our darkest shade of maple syrup.",
                                            "~/img/maple3.jpg",
                                            23.45, MapleType.DARK);
        Product productClear = new Product("4", "Maple 4",
                                            "Our clearest shade of maple syrup.",
                                            "~/img/maple4.jpg",
                                            13.8, MapleType.CLEAR);
        productRepository.save(product);
        productRepository.save(productAmber);
        productRepository.save(productDark);
        productRepository.save(productClear);

        List<Product> products = productRepository.findAllByType(MapleType.AMBER);

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);

        assertThat(products.contains(product))
                .as("Contains Amber product")
                .isTrue();
        assertThat(products.contains(productAmber))
                .as("Contains second Amber product")
                .isTrue();
        assertThat(products.contains(productClear))
                .as("Does not contain Clear product")
                .isFalse();
        assertThat(products.contains(productDark))
                .as("Does not contain Dark product.")
                .isFalse();
    }
}
