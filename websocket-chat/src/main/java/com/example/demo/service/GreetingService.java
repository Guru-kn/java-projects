package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.model.Chat;
import com.example.demo.model.HelloMessage;
import com.google.gson.Gson;

@Service
public class GreetingService {
	
	private Log log = LogFactory.getLog(GreetingService.class);
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	// @Scheduled(fixedRate = 500)
    public void sendMessage() {
		log.info("Sending message");
		String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH));
		messagingTemplate.convertAndSend("/topic/greetings", 
            new HelloMessage("CHII", "How are you at " + new Date().getTime(), dateTime));
    }
	
	public void chat(Chat chat) {
		log.info("Sending chat message");
		String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH));
		chat.setChatId(UUID.randomUUID().toString());
		chat.setDateTime(dateTime);
		log.info(new Gson().toJson(chat));
		messagingTemplate.convertAndSend("/topic/chat", chat);
    }
	
	public void isTyping(Chat chat) {
		log.info("Sending isTyping message");
		log.info(new Gson().toJson(chat));
		messagingTemplate.convertAndSend("/topic/is-typing", chat);
    }
}
