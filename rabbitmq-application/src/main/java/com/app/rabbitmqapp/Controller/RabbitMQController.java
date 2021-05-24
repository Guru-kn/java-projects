package com.app.rabbitmqapp.Controller;

import static com.app.rabbitmqapp.config.RabbitConfig.queueName;
import static com.app.rabbitmqapp.config.RabbitConfig.topicExchangeName;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.rabbitmqapp.bean.JobBean;
import com.google.gson.Gson;

@RestController
@RequestMapping("/rabbit")
public class RabbitMQController {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@PostMapping("/message")
	public ResponseEntity<JobBean> pushMessages(@RequestBody JobBean jobBean){
		
		try {
			rabbitTemplate.convertAndSend(topicExchangeName,
					queueName, jobBean);
			
			return ResponseEntity.ok(jobBean);
		}catch (Exception e) {
			return ResponseEntity.ok(new JobBean());
		}
		
		
	}
}
