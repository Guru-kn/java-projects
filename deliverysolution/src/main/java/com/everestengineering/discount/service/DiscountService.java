package com.everestengineering.discount.service;

import java.util.Map;

import org.apache.log4j.Logger;

import com.everestengineering.discount.model.DiscountCoupon;
import com.everestengineering.discount.model.DiscountCriteria;
import com.everestengineering.discount.model.DiscountResponse;
import com.everestengineering.discount.util.DiscountUtil;
import com.google.gson.Gson;

public class DiscountService {
	
	Logger logger = Logger.getLogger(DiscountService.class);
	
	public static DiscountService discountService = null;

	static {
		if (discountService == null)
			discountService = new DiscountService();
	}

	public static DiscountService getInstance() {
		DiscountUtil.getInstance();
		return discountService;
	}
	
	public DiscountResponse calculateDiscountByCouponCode(String couponCode, 
			double totalDeliveryCost, double totalWeight,
			double distanceToDestination) {
		
		DiscountResponse discountResponse =
				new DiscountResponse(false, couponCode,
				null, false, false,
				totalDeliveryCost, 0, totalWeight, distanceToDestination);
		
		Map<String, DiscountCoupon> discountCoupons = DiscountUtil.getDiscountCoupons();

		if (null != discountCoupons && null != discountCoupons.get(couponCode)) {
			DiscountCoupon discountCoupon = discountCoupons.get(couponCode);
			DiscountCriteria discountCriteria = discountCoupon.getDiscountCriteria();

			logger.info("Coupon code is found " + couponCode + ", will check if its valid or not");
			logger.info("Coupon details " + new Gson().toJson(discountCriteria));
			logger.info("totalDeliveryCost " + totalDeliveryCost);
			logger.info("totalWeight " + totalWeight);
			logger.info("distanceToDestination " + distanceToDestination);

			if (null == discountCriteria) {
				discountResponse.setCriteriaMessage("Invalid coupon code");
			} else {
				discountResponse =
						new DiscountResponse(false, couponCode,
							discountCriteria, false, false,
						totalDeliveryCost, 0, totalWeight, distanceToDestination);
			}

			switch (discountCriteria.getDiscountType()) {
			case RANGE:
				return DiscountUtil.getInstance().calculateRangeDiscount(couponCode, totalDeliveryCost,
						totalWeight, distanceToDestination,
						discountCriteria, discountResponse);
			case FLAT:
				return DiscountUtil.getInstance().calculateFlatDiscount(discountResponse);
			default:
				return discountResponse;
			}
		} else {
			discountResponse.setCriteriaMessage("Invalid coupon code");
		}
		
		return discountResponse;
	}
}
