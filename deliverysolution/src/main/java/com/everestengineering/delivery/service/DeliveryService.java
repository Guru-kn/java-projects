package com.everestengineering.delivery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.util.DeliveryUtil;
import com.google.gson.Gson;

public class DeliveryService {

	Logger logger = Logger.getLogger(DeliveryService.class);

	private DeliveryService() {

	}

	public static DeliveryService deliveryService = null;

	static {
		if (deliveryService == null)
			deliveryService = new DeliveryService();
	}

	public static DeliveryService getInstance() {
		return deliveryService;
	}

	public List<DeliveryPackage> sendPackagesToDeliver(String baseCostOfDeliveryNoOfPackages) {

		String[] baseCostToDlivrAndNoOfPckgArr = baseCostOfDeliveryNoOfPackages.split(" ");

		Scanner input = null;
		List<String> packageListToDeliver = new ArrayList<String>();
		List<DeliveryPackage> deliveredPackages = null;
		
		try {

			double baseDeliveryCost = DeliveryUtil.getInstance()
					.getDoubleValueFromStringArr(baseCostToDlivrAndNoOfPckgArr, 0);
			int numberOfPackages = Integer.valueOf(baseCostToDlivrAndNoOfPckgArr[1]);

			input = new Scanner(System.in);
			logger.info("Enter the details of " + numberOfPackages + " packages now");

			for (int i = 0; i < numberOfPackages; i++) {
				packageListToDeliver.add(input.nextLine());
			}

			List<DeliveryPackage> masterDeliveryPackageList = DeliveryUtil
					.getInstance().readPackageDetails(packageListToDeliver, baseDeliveryCost);

			deliveredPackages = new ArrayList<DeliveryPackage>();
			List<String> listOfDeliveredPckgIds = new ArrayList<String>();

			// loop over all pacakages and assign to delivery vehicle, calculate time taken
			while (masterDeliveryPackageList.size() > 0) {

				deliveredPackages = DeliveryUtil.getInstance().checkPackageDetailsAndAssignToAvailableVehicle(
						masterDeliveryPackageList, deliveredPackages, listOfDeliveredPckgIds);

				// removing the delivered item from package list to deliver
				if (deliveredPackages.size() > 0) {
					List<String> valuesToCheck = deliveredPackages.stream().map(DeliveryPackage::getPackageId)
							.collect(Collectors.toList());
					masterDeliveryPackageList = masterDeliveryPackageList.stream()
							.filter(x -> !valuesToCheck.contains(x.getPackageId())).collect(Collectors.toList());
				}
			}

			logger.info("delivered packages details " + new Gson().toJson(deliveredPackages));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		return deliveredPackages;
	}
}
