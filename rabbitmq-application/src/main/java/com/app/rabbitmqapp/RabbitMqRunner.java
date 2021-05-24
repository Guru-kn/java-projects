package com.app.rabbitmqapp;

import static com.app.rabbitmqapp.config.RabbitConfig.queueName;
import static com.app.rabbitmqapp.config.RabbitConfig.topicExchangeName;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.app.rabbitmqapp.bean.JobBean;
import com.google.gson.Gson;

@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@CrossOrigin
@SpringBootConfiguration
public class RabbitMqRunner implements CommandLineRunner {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Override
	public void run(String... args) throws Exception {
		
		JobBean jobBean = new JobBean();
		jobBean.setJobId("46732");
		jobBean.setJobStatus("Completed");
		
		rabbitTemplate.convertAndSend(topicExchangeName,
				queueName, jobBean);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(RabbitMqRunner.class, args);
	}
}
