package com.cdpipeline.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdpipeline.bean.ResponseBean;
import com.cdpipeline.repository.HelloRepository;

@Service
public class HelloService {
	
	@Autowired
	private HelloRepository helloRepository;
	
	public ResponseBean getHello() {
		
		return helloRepository.getHelloData();
	}
}
