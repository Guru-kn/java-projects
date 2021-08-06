package com.everestengineering.delivery.util;

import java.util.Comparator;

import com.everestengineering.discount.model.DeliveryVehicle;

public class DeliveryVehicleNxtAvlbleTimeComparator implements Comparator<DeliveryVehicle>{
	
	@Override
	public int compare(DeliveryVehicle o1, DeliveryVehicle o2) {
		return o1.getNextAvailableInHrs().compareTo(o2.getNextAvailableInHrs());
	}
}
