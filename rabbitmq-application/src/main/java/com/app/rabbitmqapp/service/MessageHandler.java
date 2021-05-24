package com.app.rabbitmqapp.service;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import com.app.rabbitmqapp.bean.JobBean;
import com.google.gson.Gson;

@Service
public class MessageHandler implements MessageListener{

	private static Logger log = Logger.getLogger(MessageHandler.class);

	public void onMessage(final Message message) {

		log.info(new String(message.getBody()));
		
		System.out.println(new String(message.getBody()));

		Thread thread = new Thread() {
			public void run() {
				try {
					String messageBody = new String(message.getBody());
					System.out.println(new Gson().fromJson(messageBody, JobBean.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		thread.start();
		log.info(" MESSAGE RECEIVED ");
	}
}
