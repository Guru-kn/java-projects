package com.everestengineering.model;

import com.everestengineering.constant.DiscountConstant.DiscountMeasure;
import com.everestengineering.constant.DiscountConstant.DiscountType;
import com.everestengineering.constant.DiscountConstant.RangeMeasure;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DiscountCriteria {
	
	public DiscountCriteria() {
		
	}
	
	private double fromWeight;
	private double toWeight;
	private double fromDistance;
	private double toDistance;
	private DiscountMeasure discountMeasure; // percentage or direct amount
	private double discountValue;
	private DiscountType discountType; // FLAT, RANGE etc
	private RangeMeasure rangeMeasure; // based on weight or distance or both, ie if distance 0-5 kms 5% etc
	private int totalBaseCost; // based on this value(for instance 1500rs), if disc type is flat, then delivery worth 1500 or above gets flat 10% discount
	private String couponCode;
	
	public DiscountCriteria(DiscountType discountType,
			DiscountMeasure discountMeasure, RangeMeasure rangeMeasure,
			double discountValue,double fromWeight,
			double toWeight, double fromDistance,
			double toDistance, String couponCode) {
		this.discountType = discountType;
		this.discountMeasure = discountMeasure;
		this.rangeMeasure = rangeMeasure;
		this.discountValue = discountValue;
		this.fromWeight = fromWeight;
		this.toWeight = toWeight;
		this.fromDistance = fromDistance;
		this.toDistance = toDistance;
		this.couponCode = couponCode;
	}
}	
