package com.everestengineering.discount.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.everestengineering.discount.constant.DiscountConstant.CouponNames;
import com.everestengineering.discount.constant.DiscountConstant.DiscountMeasure;
import com.everestengineering.discount.constant.DiscountConstant.DiscountType;
import com.everestengineering.discount.constant.DiscountConstant.RangeMeasure;
import com.everestengineering.discount.model.DiscountCoupon;
import com.everestengineering.discount.model.DiscountCriteria;
import com.everestengineering.discount.model.DiscountResponse;

public class DiscountUtil {

	Logger logger = Logger.getLogger(DiscountUtil.class.getName());

	private static Map<String, DiscountCoupon> discountCoupons;

	private DiscountUtil() {
		
	}
	
	public static DiscountUtil discountUtil = null;

	static {
		if (discountUtil == null)
			discountUtil = new DiscountUtil();
	}

	public static DiscountUtil getInstance() {
		if(null == discountCoupons) {
			addDiscountCoupons(); // this needs to either in cache or driven from DB
		}
		return discountUtil;
	}
	
	public static Map<String, DiscountCoupon> getDiscountCoupons() {
		if(null == discountCoupons) {
			addDiscountCoupons(); // this needs to either in cache or driven from DB
		}
		return discountCoupons;
	}

	// has to be driven from DB, but hardcoding for now 3 coupons,
	// more coupons can be added
	public static void addDiscountCoupons() {

		Map<String, DiscountCoupon> discountCoupons = new HashMap<String, DiscountCoupon>();

		// constructor values for discountCriteria
		// discountType, discountMeasure, rangeMeasure, discountValue,fromWeight,
		// toWeight, fromDistance, toDistance

		DiscountCriteria discountCriteria = new DiscountCriteria(DiscountType.RANGE, DiscountMeasure.PERCENTAGE,
				RangeMeasure.BOTH, 10, 70, 200, 1, 200, null);
		DiscountCoupon coupon = new DiscountCoupon(CouponNames.OFR001.toString(), discountCriteria);
		discountCriteria.setCouponCode(coupon.getCouponCode());
		discountCoupons.put(CouponNames.OFR001.toString(), coupon);

		discountCriteria = new DiscountCriteria(DiscountType.RANGE, DiscountMeasure.PERCENTAGE, RangeMeasure.BOTH, 7,
				100, 250, 50, 150, null);
		coupon = new DiscountCoupon(CouponNames.OFR002.toString(), discountCriteria);
		discountCriteria.setCouponCode(coupon.getCouponCode());
		discountCoupons.put(CouponNames.OFR002.toString(), coupon);

		discountCriteria = new DiscountCriteria(DiscountType.RANGE, DiscountMeasure.PERCENTAGE, RangeMeasure.BOTH, 5,
				10, 150, 50, 250, null);
		coupon = new DiscountCoupon(CouponNames.OFR003.toString(), discountCriteria);
		discountCriteria.setCouponCode(coupon.getCouponCode());
		discountCoupons.put(CouponNames.OFR003.toString(), coupon);
		
		DiscountUtil.discountCoupons = discountCoupons;
	}

	public DiscountResponse calculateRangeDiscount(String couponCode, double baseDeliveryCost, double totalWeight,
			double distanceToDestination, DiscountCriteria discountCriteria,
			DiscountResponse discountResponse){

		discountResponse = checkIfCriteriaMatches(discountCriteria,
				distanceToDestination, totalWeight, discountResponse);

		if (discountResponse.isCouponApplied()) {
			double discountInAmount = calculateDiscountBasedOnPercentageOrAmount(discountCriteria, baseDeliveryCost);
			discountResponse.setTotalDiscountInAmount(Double.valueOf(discountInAmount));
			return discountResponse;
		} else {
			discountResponse.setCriteriaMessage(
					getDiscountCriteriaMessage(couponCode, discountCriteria, distanceToDestination,
							totalWeight, discountResponse.isDistanceCriteria(),
							discountResponse.isWeightCriteria()));
		}
		
		return discountResponse;
	}

	public DiscountResponse calculateFlatDiscount(DiscountResponse discountResponse) {
		return discountResponse;
	}

	public DiscountResponse checkIfCriteriaMatches(DiscountCriteria discountCriteria,
			double distance, double weight, DiscountResponse discountResponse) {

		switch (discountCriteria.getRangeMeasure()) {
		case DISTANCE:
			discountResponse.setDistanceCriteria(distance >= discountCriteria.getFromDistance() && distance <= discountCriteria.getToDistance());
			discountResponse.setCouponApplied(true);
			break;
		case WEIGHT:
			discountResponse.setWeightCriteria(weight >= discountCriteria.getFromWeight() && weight <= discountCriteria.getToWeight());
			discountResponse.setCouponApplied(true);
			break;
		case BOTH:
			discountResponse.setDistanceCriteria(distance >= discountCriteria.getFromDistance() && distance <= discountCriteria.getToDistance());
			discountResponse.setWeightCriteria(weight >= discountCriteria.getFromWeight() && weight <= discountCriteria.getToWeight());
			discountResponse.setCouponApplied(distance >= discountCriteria.getFromDistance() && distance <= discountCriteria.getToDistance() &&
					weight >= discountCriteria.getFromWeight() && weight <= discountCriteria.getToWeight());
		default: 
			break;
		}
		
		if(discountResponse.isCouponApplied()) {
			discountResponse.setCriteriaMessage("Coupon " +
					discountCriteria.getCouponCode() + " is applied successfully");
		}
		
		return discountResponse;
	}

	public double calculateDiscountBasedOnPercentageOrAmount(DiscountCriteria discountCriteria, double totalBaseCost) {

		switch (discountCriteria.getDiscountMeasure()) {

		case PERCENTAGE:
			
			BigDecimal percentageDiscountedAmount = BigDecimal.valueOf((discountCriteria.getDiscountValue() / 100) * totalBaseCost);
			percentageDiscountedAmount = percentageDiscountedAmount.setScale(2, RoundingMode.HALF_UP);
			return percentageDiscountedAmount.doubleValue();
		case AMOUNT:
			return totalBaseCost - discountCriteria.getDiscountValue();
		default:
			return 0;
		}

	}

	public String getDiscountCriteriaMessage(String couponCode, DiscountCriteria discountCriteria,
			double totaDistance,
			double totalWeight,boolean distanceCriteria, boolean weightCriteria) {
		StringBuilder builder = new StringBuilder();
				builder.append("Coupon " + couponCode + " is not applied because ");
				
				if(discountCriteria == null) {
					builder.append("its a Invalid coupon");
					return builder.toString();
				}
				
				if(!weightCriteria) {
					builder.append("weight should be between ");
					builder.append(discountCriteria.getFromWeight());
					builder.append(" and ");
					builder.append(discountCriteria.getToWeight());
					builder.append(" in KGs ");
				}
				
				if(!distanceCriteria) {
					builder.append("distance should be between ");
					builder.append(discountCriteria.getFromDistance());
					builder.append(" and ");
					builder.append(discountCriteria.getToDistance());
					builder.append(" in KMs");
				}
				
		return builder.toString();
	}

}
