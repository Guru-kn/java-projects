package com.example.demo.model;

public class HelloMessage {

    private String name;
    private String message;
    private String dateTime;

    public HelloMessage() {}

    public HelloMessage(String name, String message, String dateTime) {
        this.name = name;
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
