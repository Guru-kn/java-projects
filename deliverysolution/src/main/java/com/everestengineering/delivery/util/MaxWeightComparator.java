package com.everestengineering.delivery.util;

import java.util.Comparator;

import com.everestengineering.delivery.model.PackageResponse;

public class MaxWeightComparator implements Comparator<PackageResponse>{
	
	@Override
	public int compare(PackageResponse res1, PackageResponse res2) {
		return res2.getMaxWeight().compareTo(res1.getMaxWeight());
	}
}
