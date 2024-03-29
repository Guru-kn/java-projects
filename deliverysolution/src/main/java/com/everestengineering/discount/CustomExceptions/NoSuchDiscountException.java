package com.everestengineering.discount.CustomExceptions;

public class NoSuchDiscountException extends Exception{
	
	private static final long serialVersionUID = 1L;
	String couponCode = "";
	
	public NoSuchDiscountException(String couponCode) {
		this.couponCode = couponCode;
	}
	
	@Override
	public String toString() {
		return this.couponCode + " coupon applied is invalid";
	}
}
