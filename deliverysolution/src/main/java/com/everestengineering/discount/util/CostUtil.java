package com.everestengineering.discount.util;

import org.apache.log4j.Logger;

import com.everestengineering.discount.constant.CostConstants;
import com.everestengineering.discount.model.DiscountResponse;
import com.everestengineering.discount.service.DiscountService;
import com.google.gson.Gson;

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
	
	public DiscountResponse calculateFinalDeliveryCost(double baseDeliveryCost,
			double weightInKg, double distanceInKms, String offerCode) {
		double baseTotalCost = CostUtil.getInstance().calculateTotalCost(baseDeliveryCost, weightInKg, distanceInKms);

		DiscountResponse discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode(offerCode, baseTotalCost,
				weightInKg, distanceInKms);
		
		logger.info(new Gson().toJson(discountResponse));
		
		double finalDeliveryCost = discountResponse.getTotalCost() - discountResponse.getTotalDiscountInAmount();
		discountResponse.setFinalDeliveryCost(finalDeliveryCost);
		
		return discountResponse;
	}
}
