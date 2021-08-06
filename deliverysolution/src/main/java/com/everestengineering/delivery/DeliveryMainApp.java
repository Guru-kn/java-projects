package com.everestengineering.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.service.DeliveryService;
import com.everestengineering.discount.model.OrderResponse;
import com.everestengineering.discount.util.ValidationUtil;

public class DeliveryMainApp {
	
	static Logger logger = Logger.getLogger(DeliveryMainApp.class);
	
	public static void main(String[] args) {
		
		Scanner input = null;
		try {
			logger.info("Enter the base delivery cost and number of packages in `100 5` format ");
			input = new Scanner(System.in);
			String baseCostOfDeliveryAndNoOfPckgs = input.nextLine();
			
			OrderResponse orderResponse = ValidationUtil.getInstance().
					validateBaseDeliveryCostAndNoOfPackages(baseCostOfDeliveryAndNoOfPckgs, new OrderResponse());
			
			if(!orderResponse.isValid()) {
				logger.info("Validation failed " + orderResponse.getValidationMessage());
			}
			
			List<String> deliveredPackages = new ArrayList<String>();
			List<DeliveryPackage>  deliveredPackageDetails = DeliveryService.getInstance().sendPackagesToDeliver(baseCostOfDeliveryAndNoOfPckgs);
			
			if(null != deliveredPackageDetails &&
					!deliveredPackageDetails.isEmpty()) {
				deliveredPackages = deliveredPackageDetails.stream()
				.map(pckg -> 
				new DeliveryPackage(
				pckg.getPackageId(), pckg.getDiscountResponse().getTotalDiscountInAmount(),
				pckg.getDiscountResponse().getFinalDeliveryCost(), pckg.getTimeTakenToDeliverInHrs()).getDeliveredPckgDetails())
				.collect(Collectors.toList());
				logger.info("O/P expected: ");
				logger.info(deliveredPackages);
			} else {
				logger.info("None of the packages has valid information to deliver, please check the packages again ");
			}
			
		}catch (Exception e) {
			logger.error(e);
		}
	}
}
