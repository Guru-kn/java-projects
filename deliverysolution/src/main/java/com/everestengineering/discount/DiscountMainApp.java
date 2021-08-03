package com.everestengineering.discount;

import com.everestengineering.discount.service.CostService;

public class DiscountMainApp {
	
	public static void main(String[] args) {
		
		CostService.getInstance().calculateTotalCostOfDelivery("100 3");
	}
}
