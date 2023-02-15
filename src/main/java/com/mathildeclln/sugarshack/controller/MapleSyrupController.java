package com.mathildeclln.sugarshack.controller;

import com.mathildeclln.sugarshack.dto.MapleSyrupDto;
import com.mathildeclln.sugarshack.service.MapleSyrupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class MapleSyrupController {
    @Autowired
    private MapleSyrupService mapleServ;

    @GetMapping("/{productId}")
    public MapleSyrupDto getProductInfo(@PathVariable String productId){
        return mapleServ.getInfo(productId);
    }
}
