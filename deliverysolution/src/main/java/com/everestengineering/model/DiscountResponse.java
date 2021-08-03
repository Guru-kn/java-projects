package com.everestengineering.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import static com.everestengineering.constant.DiscountConstant.*;

import com.everestengineering.util.DiscountUtil;

@Data
@Getter
@Setter
public class DiscountResponse {
	
	private boolean distanceCriteria;
	private boolean weightCriteria;
	private String criteriaMessage;
	private double baseCost;
	private double totalDiscountInAmount;
	private String couponCode;
	private RangeMeasure rangeMeasure;
	private boolean couponApplied;
	private double totalWeight;
	private double totalDistance;
	private DiscountCriteria discountCriteria;
	
	public DiscountResponse(boolean couponApplied, String couponCode, 
			DiscountCriteria discountCriteria,
			boolean distanceCriteria, boolean weightCriteria,double baseCost,
			double totalDiscountInAmount, double totalWeight, double totalDistance){
		
		this.couponApplied = couponApplied;
		this.couponCode = couponCode;
		this.rangeMeasure = null != discountCriteria ?
				discountCriteria.getRangeMeasure() : RangeMeasure.INVALID;
		this.distanceCriteria = distanceCriteria;
		this.weightCriteria = weightCriteria;
		this.baseCost = baseCost;
		this.totalDiscountInAmount = totalDiscountInAmount;
		this.totalWeight = totalWeight;
		this.totalDistance = totalDistance;
		this.criteriaMessage = DiscountUtil.getInstance().getDiscountCriteriaMessage(couponCode, discountCriteria, totalDistance,
				totalWeight, distanceCriteria, weightCriteria);
	}
}
