package com.everestengineering.discount.service;

import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.everestengineering.discount.model.DiscountResponse;
import com.everestengineering.discount.model.OrderResponse;
import com.everestengineering.discount.util.CostUtil;
import com.everestengineering.discount.util.ValidationUtil;
import com.google.gson.Gson;

public class CostService {
	
	Logger logger = Logger.getLogger(CostService.class);
	
	public static CostService costService = null;

	static {
		if (costService == null)
			costService = new CostService();
	}

	public static CostService getInstance() {
		return costService;
	}
	
	public String[] calculateTotalCostOfDelivery(String baseDlvryCostNoOfPackages) {
		
		// input format
		// base_delivery_cost no_of_packges
		// pkg_id1 pkg_weight1_in_kg distance1_in_km offer_code1

		// O/P: pkg_id1 discount1 total_cost1

		// String pkgIdPkgWeightInKgDistInKmOffCode = "PKG1 5 5 OFR001";
		
		OrderResponse orderResponse = ValidationUtil.getInstance().validateBaseDeliveryCostAndNoOfPackages(baseDlvryCostNoOfPackages, new OrderResponse());
		
		if(!orderResponse.isValid()) {
			logger.info("Validation failed " + orderResponse.getValidationMessage());
			return null;
		}

		String[] baseDlvryCostNoOfPackagesArr = baseDlvryCostNoOfPackages.split(" ");
		Scanner input = null;
		String[] pkgIdPkgWeightInKgDistInKmOffCode = null;
		String[] packageOutputWithDiscountAndCost = null;
		
		try {
			double baseDeliveryCost = Double.valueOf(baseDlvryCostNoOfPackagesArr[0]);
			int numberOfPackages = Integer.valueOf(baseDlvryCostNoOfPackagesArr[1]);
			
			pkgIdPkgWeightInKgDistInKmOffCode = new String[numberOfPackages];
			
			input = new Scanner(System.in);
			logger.info("Enter the details of " + numberOfPackages + " packages now");

	        for (int i = 0 ; i < pkgIdPkgWeightInKgDistInKmOffCode.length; i++ ) {
	        	pkgIdPkgWeightInKgDistInKmOffCode[i] = input.nextLine();
	        }
	        
	        packageOutputWithDiscountAndCost = new String[numberOfPackages];
	        
	        for(String packageDetails: pkgIdPkgWeightInKgDistInKmOffCode) {
	        	
	        	int index = 0;
	        	
	        	orderResponse = ValidationUtil.getInstance().
		        		validatePackageIdWeightInKgDistInKmAndOffCode(packageDetails,
		        				orderResponse);
		        
		        System.out.println(new Gson().toJson(orderResponse));
				
				if (!orderResponse.isValid()) {
					logger.info("Validation failed " + orderResponse.getValidationMessage());
					logger.info("----------------------------END OF O/P---------------------------------------");
					continue;
				}

	    		String[] pkgIdPkgWeightInKgDistInKmOffCodeArr = packageDetails.split(" ");

	    		String packageId = null;
	    		double weightInKg = 0;
	    		double distanceInKms = 0;
	    		String offerCode = null;
	    		
	    		try {
	    			packageId = pkgIdPkgWeightInKgDistInKmOffCodeArr[0].trim();
		    		weightInKg = Double.valueOf(pkgIdPkgWeightInKgDistInKmOffCodeArr[1]);
		    		distanceInKms = Double.valueOf(pkgIdPkgWeightInKgDistInKmOffCodeArr[2]);
		    		offerCode = pkgIdPkgWeightInKgDistInKmOffCodeArr[3].trim();
	    		}catch (IndexOutOfBoundsException e) {
	    			logger.log(Priority.ERROR, e.getMessage());
	    		}catch (NumberFormatException e) {
	    			logger.log(Priority.ERROR, e.getMessage() + ", Enter proper number");
	    		}catch (Exception e) {
	    			logger.log(Priority.ERROR, e.getMessage());
	    		}finally {
	    			double baseTotalCost = CostUtil.getInstance().calculateBaseCost(baseDeliveryCost, weightInKg, distanceInKms);

		    		DiscountResponse discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode(offerCode, baseTotalCost,
		    				weightInKg, distanceInKms);
		    		
		    		logger.info(new Gson().toJson(discountResponse));
		    		
		    		double totalDeliveryCost = discountResponse.getBaseCost() - discountResponse.getTotalDiscountInAmount();
		    		
		    		String pacakgeDetailWithDiscountAndCost = packageId + " " +
		    		discountResponse.getTotalDiscountInAmount() + " " + totalDeliveryCost;
		    		
		    		logger.info(pacakgeDetailWithDiscountAndCost);
		    		
		    		packageOutputWithDiscountAndCost[index] = pacakgeDetailWithDiscountAndCost;
		    		
		    		logger.info("----------------------------END OF O/P---------------------------------------");
				}
	        }
		}catch (IndexOutOfBoundsException e) {
			logger.log(Priority.ERROR, e.getMessage());
		}catch (NumberFormatException e) {
			logger.log(Priority.ERROR, e.getMessage() + ", Enter proper number");
		}catch (Exception e) {
			logger.log(Priority.ERROR, e.getMessage());
		}
		finally {
			if(input != null) {
				try {
					input.close();
				}catch (Exception e) {
					logger.log(Priority.ERROR, e.getMessage());
				}
			}
		}
		return packageOutputWithDiscountAndCost;
	}
}
