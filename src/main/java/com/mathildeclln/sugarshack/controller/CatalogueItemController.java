package com.mathildeclln.sugarshack.controller;

import com.mathildeclln.sugarshack.dto.CatalogueItemDto;
import com.mathildeclln.sugarshack.model.MapleType;
import com.mathildeclln.sugarshack.service.CatalogueItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class CatalogueItemController {

    @Autowired
    private CatalogueItemService catService;

    @GetMapping("/products")
    public ArrayList<CatalogueItemDto> getCatalogue(MapleType type){
        return catService.getCatalogue(type);
    }
}
