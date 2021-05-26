package com.app.rabbitmqapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

import com.app.rabbitmqapp.bean.JobBean;
import com.google.gson.Gson;

@Service
public class MessageHandler implements MessageListener{

	private static Logger log = LogManager.getLogger(MessageHandler.class);

	public void onMessage(final Message message) {

		log.info(new String(message.getBody()));

		Thread thread = new Thread() {
			public void run() {
				try {
					Message messageBody = MessageBuilder.withBody(message.getBody())
							.setDeliveryMode(MessageDeliveryMode.PERSISTENT)
							.build();
					String messageBodyStr = new String(messageBody.getBody());
					log.info(new Gson().fromJson(messageBodyStr, JobBean.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		thread.start();
		log.info("MESSAGE RECEIVED ");
	}
}
