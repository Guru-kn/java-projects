package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.HelloMessage;

@RestController
public class DemoController {
	
	@Autowired
	GreetingController controller;
	
	@RequestMapping(value = "/", headers = "content-type=application/json")
	public String welcome() {
		return "Hello PCF";
	}
	
	@RequestMapping(value = "/demo", headers = "content-type=application/json")
	public String helloWorld() {
		return "{\"name\":\"Guru\"}";
	}
	
	@RequestMapping(value = "/test", headers = "content-type=application/json")
	public String helloWorldRest() {
		return "{\"name\":\"Guru\"}";
	}
}
