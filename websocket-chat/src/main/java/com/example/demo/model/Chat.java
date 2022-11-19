package com.example.demo.model;

import lombok.Data;

@Data
public class Chat {
	
	private String chatId;
	private User fromUser;
	private User toUser;
	private String message;
	private String dateTime;
}
