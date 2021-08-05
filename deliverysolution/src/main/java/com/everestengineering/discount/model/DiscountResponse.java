package com.everestengineering.discount.model;

import com.everestengineering.discount.constant.DiscountConstant.RangeMeasure;
import com.everestengineering.discount.util.DiscountUtil;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DiscountResponse {
	
	private boolean distanceCriteria;
	private boolean weightCriteria;
	private String criteriaMessage;
	private double totalCost;
	private double totalDiscountInAmount;
	private String couponCode;
	private RangeMeasure rangeMeasure;
	private boolean couponApplied;
	private double totalWeight;
	private double totalDistance;
	private DiscountCriteria discountCriteria;
	private Double finalDeliveryCost;
	
	public DiscountResponse(boolean couponApplied, String couponCode, 
			DiscountCriteria discountCriteria,
			boolean distanceCriteria, boolean weightCriteria,double totalCost,
			double totalDiscountInAmount, double totalWeight, double totalDistance){
		
		this.couponApplied = couponApplied;
		this.couponCode = couponCode;
		this.rangeMeasure = null != discountCriteria ?
				discountCriteria.getRangeMeasure() : RangeMeasure.INVALID;
		this.distanceCriteria = distanceCriteria;
		this.weightCriteria = weightCriteria;
		this.totalCost = totalCost;
		this.totalDiscountInAmount = totalDiscountInAmount;
		this.totalWeight = totalWeight;
		this.totalDistance = totalDistance;
		this.criteriaMessage = DiscountUtil.getInstance().getDiscountCriteriaMessage(couponCode, discountCriteria, totalDistance,
				totalWeight, distanceCriteria, weightCriteria);
	}
}
