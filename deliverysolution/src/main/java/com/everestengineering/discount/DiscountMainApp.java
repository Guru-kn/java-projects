package com.everestengineering.discount;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.everestengineering.discount.model.OrderResponse;
import com.everestengineering.discount.service.CostService;
import com.everestengineering.discount.util.ValidationUtil;

public class DiscountMainApp {
	
	static Logger logger = Logger.getLogger(DiscountMainApp.class);
	
	public static void main(String[] args) {
		
		Scanner input = null;
		
		// input format
		// base_delivery_cost no_of_packges
		// pkg_id1 pkg_weight1_in_kg distance1_in_km offer_code1

		// O/P: pkg_id1 discount1 total_cost1

		// String pkgIdPkgWeightInKgDistInKmOffCode = "PKG1 5 5 OFR001";
		try {
			logger.info("Enter the base delivery cost and number of packages in `100 5` format ");
			input = new Scanner(System.in);
			String baseCostOfDeliveryAndNoOfPckgs = input.nextLine();
			
			OrderResponse orderResponse = ValidationUtil.
					validateBaseDeliveryCostAndNoOfPackages(baseCostOfDeliveryAndNoOfPckgs, new OrderResponse());
			
			if(!orderResponse.isValid()) {
				logger.info("Validation failed " + orderResponse.getValidationMessage());
			}
			
			String[] baseDlvryCostNoOfPackagesArr = baseCostOfDeliveryAndNoOfPckgs.split(" ");
			String[] pkgIdPkgWeightInKgDistInKmOffCode = null;
			
			double baseDeliveryCost = Double.valueOf(baseDlvryCostNoOfPackagesArr[0]);
			int numberOfPackages = Integer.valueOf(baseDlvryCostNoOfPackagesArr[1]);
			
			pkgIdPkgWeightInKgDistInKmOffCode = new String[numberOfPackages];
			
			input = new Scanner(System.in);
			logger.info("Enter the details of " + numberOfPackages + " packages now");

	        for (int i = 0 ; i < pkgIdPkgWeightInKgDistInKmOffCode.length; i++ ) {
	        	pkgIdPkgWeightInKgDistInKmOffCode[i] = input.nextLine();
	        }
			
			String[] packageOutputWithDiscountAndCost = CostService.getInstance().calculateTotalCostOfDelivery(baseDeliveryCost,
					pkgIdPkgWeightInKgDistInKmOffCode);
			logger.info("----------Final O/P---------- ");
			for(String op: packageOutputWithDiscountAndCost) {
				System.out.println(op);
			}
			
		}catch (Exception e) {
			logger.error(e);
		}finally {
			if(input != null) {
				try {
					input.close();
				}catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
	}
}
