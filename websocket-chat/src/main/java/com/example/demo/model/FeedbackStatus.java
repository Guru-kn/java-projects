package com.example.demo.model;

public enum FeedbackStatus {

    READ("Read"),
    UNREAD("Unread"),
    ARCHIVED("Archived");
	
	private final String label;
    private String key;

    FeedbackStatus(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
