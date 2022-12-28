package com.munan.feeniestore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeResource {

    @GetMapping("/hello")
    public String greeting(){
        return "Hello World".toUpperCase();
    }
}
