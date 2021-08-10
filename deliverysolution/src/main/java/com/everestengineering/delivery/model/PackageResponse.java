package com.everestengineering.delivery.model;

import lombok.Data;

@Data
public class PackageResponse {
	
	private Integer maxWeight;
	private int[] indexOfMaxSum;
	private Integer numberOfPackages;
	private double totalDistance;
	
	public PackageResponse() {
		
	}
	
	public PackageResponse(Integer maxWeight, int[] indexOfMaxSum, Integer numberOfPackages, Double totalDistance) {
		
		this.maxWeight = maxWeight;
		this.indexOfMaxSum = indexOfMaxSum;
		this.numberOfPackages = numberOfPackages;
		this.totalDistance = totalDistance;
	}
}
