package com.mathildeclln.sugarshack.controller;

import com.mathildeclln.sugarshack.dto.CatalogueItemDto;
import com.mathildeclln.sugarshack.dto.MapleSyrupDto;
import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.service.CatalogueItemService;
import com.mathildeclln.sugarshack.service.MapleSyrupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private MapleSyrupService mapleSyrupService;
    @Autowired
    private CatalogueItemService catalogueItemService;

    @GetMapping
    public ArrayList<CatalogueItemDto> getCatalogue(MapleType type) {

        return catalogueItemService.getCatalogue(type);
    }

    @GetMapping("/{productId}")
    public MapleSyrupDto getProductInfo(@PathVariable String productId){

        return mapleSyrupService.getInfo(productId);
    }
}
