package com.app.rabbitmqapp.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class JobBean implements Serializable{
	
	private static final long serialVersionUID = -2305150190935560621L;

	private String jobId;
	private String jobStatus;
}
