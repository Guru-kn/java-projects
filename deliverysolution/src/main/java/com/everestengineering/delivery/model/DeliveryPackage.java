package com.everestengineering.delivery.model;

import com.everestengineering.discount.model.DiscountCoupon;
import com.everestengineering.discount.model.DiscountResponse;
import com.everestengineering.discount.util.DiscountUtil;

import lombok.Data;

@Data
public class DeliveryPackage {
	
	private String packageId;
	private Double weightInKg;
	private Double distanceInKms;
	private Double timeTakenToDeliverInHrs;
	private Double baseCostOfDelivery;
	private Double totalCostToDeliver;
	private DiscountCoupon discountCoupon;
	private DiscountResponse discountResponse;
	private String deliveredPckgDetails;
	
	public DeliveryPackage() {
		
	}
	
	public DeliveryPackage(String packageDetails, double baseCostToDeliver) {
		//PKG1 50 30 OFR001
		String[] pkgIdPkgWeightInKgDistInKmOffCodeArr = packageDetails.split(" ");
		this.packageId = pkgIdPkgWeightInKgDistInKmOffCodeArr[0].trim();
		this.weightInKg = Double.valueOf(pkgIdPkgWeightInKgDistInKmOffCodeArr[1]);
		this.distanceInKms = Double.valueOf(pkgIdPkgWeightInKgDistInKmOffCodeArr[2]);
		this.baseCostOfDelivery = baseCostToDeliver;

		String offerCode = pkgIdPkgWeightInKgDistInKmOffCodeArr[3].trim();
		this.discountCoupon = DiscountUtil.getDiscountCoupons().get(offerCode);
	}
	
	public DeliveryPackage(String pckId, Double discount,
			Double finalDeliveryCost, Double totalTimeToDeliverPckInHrs) {
		
		this.deliveredPckgDetails = pckId + " " + discount + " " +
				finalDeliveryCost + " " + totalTimeToDeliverPckInHrs;
	}
}
