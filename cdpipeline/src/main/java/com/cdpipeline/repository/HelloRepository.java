package com.cdpipeline.repository;

import org.springframework.stereotype.Repository;

import com.cdpipeline.bean.ResponseBean;

@Repository
public class HelloRepository {
	
	public ResponseBean getHelloData() {
		
		ResponseBean responseBean = new ResponseBean();
		responseBean.setName("Guru");
		responseBean.setEmpId(12);
		
		return responseBean;
	}
}
