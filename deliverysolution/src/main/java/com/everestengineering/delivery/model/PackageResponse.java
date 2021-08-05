package com.everestengineering.delivery.model;

import lombok.Data;

@Data
public class PackageResponse {
	
	private Integer maxWeight;
	private int[] indexOfMaxSum;
	private Integer numberOfPackages;
}
