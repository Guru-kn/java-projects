package com.javageek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DeliveryApp {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryApp.class, args);
	}

}
