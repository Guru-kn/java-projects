package com.everestengineering.discount.model;

import java.util.List;

import com.everestengineering.delivery.constant.DeliveryConstant;
import com.everestengineering.delivery.model.DeliveryPackage;

import lombok.Data;

@Data
public class DeliveryVehicle {
	
	private String vId;
	private Boolean isAvailable;
	private Integer nextAvailableInHrs;
	private Double maxSpeedInKms = DeliveryConstant.MAX_SPEED_PER_HR_IN_KMS;
	private Double maxLoadTakenInKgs = DeliveryConstant.MAX_WEIGHT_LOAD_IN_KGS;
	private List<DeliveryPackage> deliveryPackages;
	
	public DeliveryVehicle() {
		
	}
	
	public DeliveryVehicle(String vId, Boolean isAvailable, Integer nextAvailableInHrs) {
		
		this.vId = vId;
		this.isAvailable = isAvailable;
		this.nextAvailableInHrs = nextAvailableInHrs;
	}
}
