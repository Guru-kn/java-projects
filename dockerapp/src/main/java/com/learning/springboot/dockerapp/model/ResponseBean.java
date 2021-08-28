package com.learning.springboot.dockerapp.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResponseBean {
	
	private String name;
	private Long id;
}
