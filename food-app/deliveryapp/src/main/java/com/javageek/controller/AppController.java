package com.javageek.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javageek.dto.OrderResponseDTO;
import com.javageek.service.AppService;

@RestController
@RequestMapping("/delivery")
public class AppController {

    @Autowired
    private AppService service;

    @GetMapping("/home")
    public String greetingMessage() {
        return service.greeting();
    }

    @GetMapping("/order/{orderId}")
    public OrderResponseDTO checkOrderStatus(@PathVariable String orderId) {
        return service.checkOrderStatus(orderId);
    }
}
