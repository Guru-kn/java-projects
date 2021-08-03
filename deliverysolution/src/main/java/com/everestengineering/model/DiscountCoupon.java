package com.everestengineering.model;

import lombok.Data;

@Data
public class DiscountCoupon {
	
	private String couponCode;
	private DiscountCriteria discountCriteria;
	
	public DiscountCoupon(){
		
	}
	
	public DiscountCoupon(String couponCode, DiscountCriteria discountCriteria) {
		
		this.couponCode = couponCode;
		this.discountCriteria = discountCriteria;
	}
}
