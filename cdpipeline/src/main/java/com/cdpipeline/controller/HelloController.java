package com.cdpipeline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdpipeline.bean.ResponseBean;
import com.cdpipeline.service.HelloService;

import io.swagger.annotations.ApiOperation;

@RestController
public class HelloController {
	
	@Autowired
	private HelloService helloService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
    @RequestMapping("/hello")
    @ApiOperation("Hello API")
    public ResponseBean index1() {
        return helloService.getHello();
    }

}