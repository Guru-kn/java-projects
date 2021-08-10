package com.everestengineering.discount.model;

import java.util.List;

import com.everestengineering.delivery.constant.DeliveryConstant;
import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.util.VehicleUtil;

import lombok.Data;

@Data
public class DeliveryVehicle{
	
	private String vId;
	private Boolean isAvailable;
	private Double nextAvailableInHrs;
	private Double maxSpeedInKms = VehicleUtil.maxSpeed;
	private Double maxLoadTakenInKgs = VehicleUtil.maxCarriableWeight;
	private List<DeliveryPackage> deliveryPackages;
	
	public DeliveryVehicle() {
		
	}
	
	public DeliveryVehicle(String vId, Boolean isAvailable, Double nextAvailableInHrs) {
		this.vId = vId;
		this.isAvailable = isAvailable;
		this.nextAvailableInHrs = nextAvailableInHrs;
	}
}
