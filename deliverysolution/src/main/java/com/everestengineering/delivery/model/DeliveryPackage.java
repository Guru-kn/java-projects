package com.everestengineering.delivery.model;

import com.everestengineering.discount.model.DiscountCoupon;
import com.everestengineering.discount.model.DiscountCriteria;
import com.everestengineering.discount.util.DiscountUtil;

import lombok.Data;

@Data
public class DeliveryPackage {
	
	private String packageId;
	private Double weightInKg;
	private Double distanceInKms;
	private DiscountCoupon discountCoupon;
	
	public DeliveryPackage() {
		
	}
	
	public DeliveryPackage(String packageDetails) {
		//PKG1 50 30 OFR001
		String[] pkgIdPkgWeightInKgDistInKmOffCodeArr = packageDetails.split(" ");
		this.packageId = pkgIdPkgWeightInKgDistInKmOffCodeArr[0].trim();
		this.weightInKg = Double.valueOf(pkgIdPkgWeightInKgDistInKmOffCodeArr[1]);
		this.distanceInKms = Double.valueOf(pkgIdPkgWeightInKgDistInKmOffCodeArr[2]);

		String offerCode = pkgIdPkgWeightInKgDistInKmOffCodeArr[3].trim();
		this.discountCoupon = DiscountUtil.getDiscountCoupons().get(offerCode);
	}
}
