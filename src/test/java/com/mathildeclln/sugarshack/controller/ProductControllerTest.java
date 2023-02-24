package com.mathildeclln.sugarshack.controller;

import com.mathildeclln.sugarshack.dto.CatalogueItemDto;
import com.mathildeclln.sugarshack.dto.MapleSyrupDto;
import com.mathildeclln.sugarshack.exception.ProductNotFoundException;
import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.service.CatalogueItemService;
import org.junit.jupiter.api.Test;
import com.mathildeclln.sugarshack.service.MapleSyrupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MapleSyrupService mapleSyrupService;

    @MockBean
    private CatalogueItemService catalogueItemService;

    @Test
    public void getProductInfoTest() throws Exception {
        MapleSyrupDto mapleSyrup = new MapleSyrupDto("1", "Maple1", "...",
                                        "img", 10, 7, MapleType.AMBER);

        given(mapleSyrupService.getInfo(mapleSyrup.getId())).willReturn(mapleSyrup);

        ResultActions result = mockMvc.perform(get("/products/{productId}", mapleSyrup.getId()));

        result.andExpect(status().isOk()).andDo(print());
        result.andExpect(jsonPath("$.id").value(mapleSyrup.getId()));
        result.andExpect(jsonPath("$.name").value(mapleSyrup.getName()));
        result.andExpect(jsonPath("$.description").value(mapleSyrup.getDescription()));
        result.andExpect(jsonPath("$.image").value(mapleSyrup.getImage()));
        result.andExpect(jsonPath("$.price").value(mapleSyrup.getPrice()));
        result.andExpect(jsonPath("$.stock").value(mapleSyrup.getStock()));
        result.andExpect(jsonPath("$.type").value(mapleSyrup.getType().name()));
    }

    @Test
    public void getProductInfoNotFoundExceptionTest() throws Exception{
        given(mapleSyrupService.getInfo("4")).willThrow(ProductNotFoundException.class);

        ResultActions result = mockMvc.perform(get("/products/{productId}", "4"));

        result.andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void getCatalogue() throws Exception {
        CatalogueItemDto itemClear = new CatalogueItemDto("2", "Maple2", "img2",
                                                        15, 10, MapleType.CLEAR);
        CatalogueItemDto itemClear2 = new CatalogueItemDto("3", "Maple3", "img3",
                                                        13, 20, MapleType.CLEAR);

        ArrayList<CatalogueItemDto> catalogueItems = new ArrayList<>();
        catalogueItems.add(itemClear);
        catalogueItems.add(itemClear2);

        given(catalogueItemService.getCatalogue(itemClear.getType())).willReturn(catalogueItems);

        ResultActions result = mockMvc.perform(get("/products")
                                                .param("type", itemClear.getType().name()));

        result.andExpect(status().isOk()).andDo(print());
        result.andExpect(jsonPath("$.length()").value(2));

        result.andExpect(jsonPath("$[0].id").value(itemClear.getId()));
        result.andExpect(jsonPath("$[0].name").value(itemClear.getName()));
        result.andExpect(jsonPath("$[0].image").value(itemClear.getImage()));
        result.andExpect(jsonPath("$[0].price").value(itemClear.getPrice()));
        result.andExpect(jsonPath("$[0].maxQty").value(itemClear.getMaxQty()));
        result.andExpect(jsonPath("$[0].type").value(itemClear.getType().name()));

        result.andExpect(jsonPath("$[1].id").value(itemClear2.getId()));
        result.andExpect(jsonPath("$[1].name").value(itemClear2.getName()));
        result.andExpect(jsonPath("$[1].image").value(itemClear2.getImage()));
        result.andExpect(jsonPath("$[1].price").value(itemClear2.getPrice()));
        result.andExpect(jsonPath("$[1].maxQty").value(itemClear2.getMaxQty()));
        result.andExpect(jsonPath("$[1].type").value(itemClear2.getType().name()));
    }

    @Test
    public void getCatalogueNotFoundExceptionTest() throws Exception{
        given(catalogueItemService.getCatalogue(MapleType.DARK)).willThrow(ProductNotFoundException.class);

        ResultActions result = mockMvc.perform(get("/products")
                .param("type", MapleType.DARK.name()));

        result.andExpect(status().isNotFound()).andDo(print());
    }

}
