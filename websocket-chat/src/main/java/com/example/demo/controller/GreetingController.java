package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Chat;
import com.example.demo.model.Greeting;
import com.example.demo.model.HelloMessage;
import com.example.demo.service.GreetingService;
import com.google.gson.Gson;

@RestController
public class GreetingController {
	
	@Autowired
	private GreetingService service;

//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Greeting greeting(HelloMessage message) throws Exception {
//        // Thread.sleep(1000); // simulated delay
//        return new Greeting(new Gson().toJson(message));
//    }
    
    @PostMapping("/chat")
    public void chat(@RequestBody Chat chat) throws Exception {
        service.chat(chat);
    }
    
    @PostMapping("/action")
    public void isTyping(@RequestBody Chat chat) throws Exception {
        service.isTyping(chat);
    }

}
