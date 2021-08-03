package com.everestengineering.constant;

public interface DiscountConstant {
	
	enum DiscountType  {
		FLAT, RANGE
	}
	
	enum DiscountMeasure {
		PERCENTAGE, AMOUNT
	}
	
	enum RangeMeasure {
		WEIGHT, DISTANCE, BOTH, INVALID
	}
	
	enum CouponNames {
		OFR001, OFR002, OFR003
	}
	
	String COUPON_CODE = "Coupon code";
}
