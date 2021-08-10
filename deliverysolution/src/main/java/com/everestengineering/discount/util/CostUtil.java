package com.everestengineering.discount.util;

import org.apache.log4j.Logger;

import com.everestengineering.discount.constant.CostConstants;

public class CostUtil {

	Logger logger = Logger.getLogger(CostUtil.class);
	
	public static CostUtil costUtil = null;

	static {
		if (costUtil == null)
			costUtil = new CostUtil();
	}

	public static CostUtil getInstance() {
		return costUtil;
	}

	public double calculateTotalCost(double baseDeliveryCost, double totalWeight, double distanceToDestination) {

		double totalCost = baseDeliveryCost + (totalWeight * CostConstants.COST_PER_KG)
				+ (distanceToDestination * CostConstants.COST_PER_KM);
		return totalCost;
	}
}
