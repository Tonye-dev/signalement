package com.gestion.ApplicationSignalement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public String publicTest() {
        return "Public OK";
    }

    @GetMapping("/private")
    public String privateTest() {
        return "Private OK";
    }
}

