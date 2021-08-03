package com.everestengineering.model;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrderResponse {
	
	private boolean isValid;
	private String validationMessage;
	private String packageId;
	private String couponCode;
	
	private List<OrderResponse> orderResponseList;
}
