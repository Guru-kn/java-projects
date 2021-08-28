package com.learning.springboot.dockerapp.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.learning.springboot.dockerapp.model.ResponseBean;

@RestController
public class TestController {

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/employee",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseBean> employeeDetails() {
		
		ResponseBean bean = new ResponseBean();
		bean.setId(2L);
		bean.setName("Guru");
		
		return ResponseEntity.ok(bean);
	}
}
