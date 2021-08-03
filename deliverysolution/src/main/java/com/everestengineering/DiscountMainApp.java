package com.everestengineering;

import com.everestengineering.service.CostService;

public class DiscountMainApp {
	
	public static void main(String[] args) {
		
		CostService.getInstance().calculateTotalCostOfDelivery("100 3");
	}
}
