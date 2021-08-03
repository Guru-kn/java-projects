package com.everestengineering.util;

import org.apache.log4j.Logger;

import com.everestengineering.constant.CostConstants;

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

	public double calculateBaseCost(double baseDeliveryCost, double totalWeight, double distanceToDestination) {

		double totalBaseCost = baseDeliveryCost + (totalWeight * CostConstants.COST_PER_KG)
				+ (distanceToDestination * CostConstants.COST_PER_KM);
		return totalBaseCost;
	}
}
