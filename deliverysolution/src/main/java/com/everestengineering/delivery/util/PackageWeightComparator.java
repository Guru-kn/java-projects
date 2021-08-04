package com.everestengineering.delivery.util;

import java.util.Comparator;

import com.everestengineering.delivery.model.DeliveryPackage;

public class PackageWeightComparator implements Comparator<DeliveryPackage>{
	
	@Override
	public int compare(DeliveryPackage package1, DeliveryPackage package2) {
		return package1.getWeightInKg().compareTo(package2.getWeightInKg());
	}
}
