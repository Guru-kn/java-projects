package com.everestengineering.CustomExceptions;

public class NoSuchDiscountException extends Exception{
	
	private static final long serialVersionUID = 1L;
	String couponCode = "";
	
	public NoSuchDiscountException(String couponCode) {
		this.couponCode = couponCode;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.couponCode + " coupon applied is invalid";
	}
}
