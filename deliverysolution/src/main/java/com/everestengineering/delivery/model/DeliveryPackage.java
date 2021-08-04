package com.everestengineering.delivery.model;

import com.everestengineering.discount.model.DiscountCoupon;

import lombok.Data;

@Data
public class DeliveryPackage {
	
	private String packageId;
	private Double weightInKg;
	private Double distanceInKm;
	private DiscountCoupon discountCoupon;
}
