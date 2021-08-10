package com.everestengineering.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.service.DeliveryService;
import com.everestengineering.delivery.util.DeliveryUtil;
import com.everestengineering.delivery.util.VehicleUtil;
import com.everestengineering.discount.model.OrderResponse;
import com.everestengineering.discount.util.ValidationUtil;
import com.google.gson.Gson;

public class DeliveryMainApp {
	
	static Logger logger = Logger.getLogger(DeliveryMainApp.class);
	
	public static void main(String[] args) {
		
		Scanner input = null;
		try {
			logger.info("Enter the base delivery cost and number of packages in `100 5` format ");
			input = new Scanner(System.in);
			String baseCostOfDeliveryAndNoOfPckgs = input.nextLine();
			
			String[] baseCostToDlivrAndNoOfPckgArr = baseCostOfDeliveryAndNoOfPckgs.split(" ");
			
			OrderResponse orderResponse = ValidationUtil.
					validateBaseDeliveryCostAndNoOfPackages(baseCostOfDeliveryAndNoOfPckgs, new OrderResponse());
			
			if(!orderResponse.isValid()) {
				logger.info("Validation failed " + orderResponse.getValidationMessage());
			}
			
			double baseDeliveryCost = DeliveryUtil.getDoubleValueFromStringArr(baseCostToDlivrAndNoOfPckgArr, 0);
			int numberOfPackages = Integer.valueOf(baseCostToDlivrAndNoOfPckgArr[1]);

			input = new Scanner(System.in);
			logger.info("Enter the details of " + numberOfPackages + " packages now");
			
			List<String> packageListToDeliver = new ArrayList<String>();
			for (int i = 0; i < numberOfPackages; i++) {
				packageListToDeliver.add(input.nextLine());
			}
			
			input = new Scanner(System.in);
			logger.info("Enter the details of vehicle in format no_of_vehicles max_speed max_carriable_weight ");
			
			String vehicleDetails = input.nextLine();
			orderResponse = ValidationUtil.validateVehicleDetails(vehicleDetails, orderResponse);
			
			if(!orderResponse.isValid()) {
				logger.info("Validation failed " + orderResponse.getValidationMessage());
			}
			
			VehicleUtil.addDeliveryVehicles(vehicleDetails);
			
			System.out.println(new Gson().toJson(VehicleUtil.getVehicleFleets()));
			
			List<String> deliveredPackages = null;
			List<DeliveryPackage>  deliveredPackageDetails = DeliveryService.getInstance().
					sendPackagesToDeliver(baseDeliveryCost, packageListToDeliver);
			
			if(null != deliveredPackageDetails &&
					!deliveredPackageDetails.isEmpty()) {
				deliveredPackages = DeliveryUtil.deliveryPackageListToExpectedFormat(deliveredPackageDetails);
				logger.info(deliveredPackages);
			} else {
				logger.info("None of the packages has valid information to deliver, please check the packages again ");
			}
			
		}catch (Exception e) {
			logger.error(e);
		}
	}
}
