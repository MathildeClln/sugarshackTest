package com.mathildeclln.sugarshack.service;

import com.mathildeclln.sugarshack.dto.CatalogueItemDto;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CatalogueItemServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private StockRepository stockRepository;

    private ArrayList<Product> products;
    private ArrayList<Stock> stocks;
    private ArrayList<CatalogueItemDto> catalogueItems;

    @InjectMocks
    private CatalogueItemService catalogueItemService;

    @BeforeEach
    public void initialSteps(){
        productRepository = Mockito.mock(ProductRepository.class);
        stockRepository = Mockito.mock(StockRepository.class);
        catalogueItemService = new CatalogueItemService(productRepository, stockRepository);
        products = new ArrayList<>();
        stocks = new ArrayList<>();

        Product product1 = new Product("1", "Maple1", "...",
                                    "img1.jpg", 12.99, MapleType.AMBER);
        Stock stock1 = new Stock("1", 10);
        Product product2 = new Product("2", "Maple2", "...",
                                    "img2.jpg", 14.5, MapleType.AMBER);
        Stock stock2 = new Stock("2", 15);

        products.add(product1);
        products.add(product2);
        stocks.add(stock1);
        stocks.add(stock2);
    }

    @Test
    public void getCatalogueHappyPathTest(){
        given(productRepository.findAllByType(MapleType.AMBER)).willReturn(products);
        given(stockRepository.findByProductId(stocks.get(0).getProductId())).willReturn(stocks.get(0));
        given(stockRepository.findByProductId(stocks.get(1).getProductId())).willReturn(stocks.get(1));

        catalogueItems = catalogueItemService.getCatalogue(MapleType.AMBER);

        assertThat(catalogueItems).isNotNull();
        assertThat(catalogueItems.size()).isEqualTo(2);

        assertThat(catalogueItems.get(0).getId()).isEqualTo(products.get(0).getId());
        assertThat(catalogueItems.get(0).getPrice()).isEqualTo(products.get(0).getPrice());
        assertThat(catalogueItems.get(0).getMaxQty()).isEqualTo(stocks.get(0).getStock());
        assertThat(catalogueItems.get(0).getName()).isEqualTo(products.get(0).getName());
        assertThat(catalogueItems.get(0).getImage()).isEqualTo(products.get(0).getImage());
        assertThat(catalogueItems.get(0).getType()).isEqualTo(products.get(0).getType());

        assertThat(catalogueItems.get(1).getId()).isEqualTo(products.get(1).getId());
        assertThat(catalogueItems.get(1).getPrice()).isEqualTo(products.get(1).getPrice());
        assertThat(catalogueItems.get(1).getMaxQty()).isEqualTo(stocks.get(1).getStock());
        assertThat(catalogueItems.get(1).getName()).isEqualTo(products.get(1).getName());
        assertThat(catalogueItems.get(1).getImage()).isEqualTo(products.get(1).getImage());
        assertThat(catalogueItems.get(1).getType()).isEqualTo(products.get(1).getType());
    }

    @Test
    public void getCatalogueEmptyTest(){
        ArrayList<Product>          emptyProductList = new ArrayList<>();

        given(productRepository.findAllByType(MapleType.DARK)).willReturn(emptyProductList);

        catalogueItems = catalogueItemService.getCatalogue(MapleType.DARK);

        assertThat(catalogueItems).isNotNull();
        assertThat(catalogueItems.isEmpty()).isTrue();
    }

    @Test
    public void getCatalogueExceptionTest(){
        ArrayList<Product> exceptionProductList = new ArrayList<>();
        exceptionProductList.add(new Product("3", "Maple3", "...",
                                            "image3.jpg", 10.7, MapleType.CLEAR));

        given(productRepository.findAllByType(exceptionProductList.get(0).getType())).willReturn(exceptionProductList);
        given(stockRepository.findByProductId(exceptionProductList.get(0).getId())).willReturn(null);

        assertThrows(ProductNotFoundException.class,
                () -> catalogueItemService.getCatalogue(exceptionProductList.get(0).getType()));
    }
}
