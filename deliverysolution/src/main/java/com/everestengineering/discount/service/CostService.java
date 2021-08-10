package com.everestengineering.discount.service;

import org.apache.log4j.Logger;

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
	
	public String[] calculateTotalCostOfDelivery(double baseDeliveryCost, String[] pkgIdPkgWeightInKgDistInKmOffCode) {

		String[] packageOutputWithDiscountAndCost = null;
		OrderResponse orderResponse = null;
		
		try {
	        
	        packageOutputWithDiscountAndCost = new String[pkgIdPkgWeightInKgDistInKmOffCode.length];
	        
	        int index = 0;
	        for(String packageDetails: pkgIdPkgWeightInKgDistInKmOffCode) {
	        	
	        	orderResponse = ValidationUtil.
		        		validatePackageIdWeightInKgDistInKmAndOffCode(packageDetails,
		        				orderResponse);
		        
		        logger.info(new Gson().toJson(orderResponse));
				
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
	    			logger.error(e.getMessage());
	    		}catch (NumberFormatException e) {
	    			logger.error(e.getMessage() + ", Enter proper number");
	    		}catch (Exception e) {
	    			logger.error(e.getMessage());
	    		}finally {
	    			double baseTotalCost = CostUtil.getInstance().calculateTotalCost(baseDeliveryCost, weightInKg, distanceInKms);

		    		DiscountResponse discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode(offerCode, baseTotalCost,
		    				weightInKg, distanceInKms);
		    		
		    		logger.info(new Gson().toJson(discountResponse));
		    		
		    		double totalDeliveryCost = discountResponse.getTotalCost() - discountResponse.getTotalDiscountInAmount();
		    		
		    		String pacakgeDetailWithDiscountAndCost = packageId + " " +
		    		discountResponse.getTotalDiscountInAmount() + " " + totalDeliveryCost;
		    		
		    		logger.info(pacakgeDetailWithDiscountAndCost);
		    		
		    		packageOutputWithDiscountAndCost[index] = pacakgeDetailWithDiscountAndCost;
		    		index++;
		    		logger.info("----------------------------END OF O/P---------------------------------------");
				}
	        }
		}catch (IndexOutOfBoundsException e) {
			logger.error(e.getMessage());
		}catch (NumberFormatException e) {
			logger.error(e.getMessage() + ", Enter proper number");
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return packageOutputWithDiscountAndCost;
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
