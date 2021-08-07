package com.everestengineering.delivery.model;

import lombok.Data;

@Data
public class PackageResponse {
	
	private Integer maxWeight;
	private int[] indexOfMaxSum;
	private Integer numberOfPackages;
	
	public PackageResponse() {
		
	}
	
	public PackageResponse(Integer maxWeight, int[] indexOfMaxSum, Integer numberOfPackages) {
		
		this.maxWeight = maxWeight;
		this.indexOfMaxSum = indexOfMaxSum;
		this.numberOfPackages = numberOfPackages;
	}
}
