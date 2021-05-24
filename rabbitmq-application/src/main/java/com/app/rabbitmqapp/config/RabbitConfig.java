package com.app.rabbitmqapp.config;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.app.rabbitmqapp.service.MessageHandler;

@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class RabbitConfig {

	private static Logger LOGGER = Logger.getLogger(RabbitConfig.class);

	public final static String queueName = "message-queue";
	
	public final static String topicExchangeName = "message-exchange";

	@Value("${rabbitmq.host}")
	public String RABBIT_HOST;

	@Value("${rabbitmq.username}")
	public String RABBIT_USERNAME;

	@Value("${rabbitmq.password}")
	private String RABBIT_PASSWORD;

	@Bean
	public ConnectionFactory connectionFactory() {

		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(RABBIT_HOST);
		connectionFactory.setUsername(RABBIT_USERNAME);
		connectionFactory.setPassword(RABBIT_PASSWORD);
		return connectionFactory;
	}

	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setDefaultReceiveQueue(queueName);
		template.setMessageConverter(jsonMessageConverter());
		template.setRoutingKey(topicExchangeName);
		return template;
	}

	@Bean
	Queue queue() {
		return new Queue(queueName, true);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(topicExchangeName);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter(){
		Jackson2JsonMessageConverter con= new Jackson2JsonMessageConverter();
		return con;
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);

		LOGGER.info("Rabbit host : " + connectionFactory.getHost());
		LOGGER.info("Rabbit host : " + connectionFactory.getPort());
		LOGGER.info("Rabbit user name : " + connectionFactory.getUsername());

		return container;
	}

	@Bean
	MessageHandler receiver() {
		return new MessageHandler();
	}

	@Bean
	MessageListenerAdapter listenerAdapter(MessageHandler receiver) {
		return new MessageListenerAdapter(receiver, "onMessage");
	}
} 
