package com.everestengineering.delivery.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.util.DeliveryUtil;
import com.everestengineering.delivery.util.PackageWeightComparator;
import com.everestengineering.discount.model.DeliveryVehicle;
import com.everestengineering.discount.model.OrderResponse;
import com.everestengineering.discount.util.ValidationUtil;
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

	public List<DeliveryPackage> sendPackagesToDeliver(double baseDeliveryCost, List<String> packageListToDeliver) {

		List<DeliveryPackage> deliveredPackages = null;
		
		try {

			List<DeliveryPackage> masterDeliveryPackageList = readPackageDetails(packageListToDeliver, baseDeliveryCost);

			deliveredPackages = new ArrayList<DeliveryPackage>();
			List<String> listOfDeliveredPckgIds = new ArrayList<String>();

			// loop over all pacakages and assign to delivery vehicle, calculate time taken
			while (masterDeliveryPackageList.size() > 0) {

				deliveredPackages = checkPackageDetailsAndAssignToAvailableVehicle(
						masterDeliveryPackageList, deliveredPackages, listOfDeliveredPckgIds);

				// removing the delivered item from package list to deliver
				if (deliveredPackages.size() > 0) {
					List<String> valuesToCheck = deliveredPackages.stream().map(DeliveryPackage::getPackageId)
							.collect(Collectors.toList());
					masterDeliveryPackageList = masterDeliveryPackageList.stream()
							.filter(x -> !valuesToCheck.contains(x.getPackageId())).collect(Collectors.toList());
				}
			}

			logger.debug("delivered packages details " + new Gson().toJson(deliveredPackages));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		return deliveredPackages;
	}
	
	public List<DeliveryPackage> readPackageDetails(List<String> packageList, Double baseCostOfDelivery) {

		List<DeliveryPackage> deliveryPackageList = new ArrayList<DeliveryPackage>();

		DeliveryPackage deliveryPackage = null;
		for (String pckage : packageList) {
			
			OrderResponse orderResponse = ValidationUtil.
			validatePackageIdWeightInKgDistInKmAndOffCode(pckage, new OrderResponse());
			
			// packages wont be added to fleet if details has some issue
			if(!orderResponse.isValid()) {
				logger.debug("Validation failed " + orderResponse.getValidationMessage());
				logger.debug("----------------------------END OF O/P---------------------------------------");
				continue;
			}

			deliveryPackage = new DeliveryPackage(pckage, baseCostOfDelivery);
			deliveryPackageList.add(deliveryPackage);
		}

		// 50, 75, 175, 110, 155 -> 50, 75, 110, 155, 175
		Collections.sort(deliveryPackageList, new PackageWeightComparator());
		return deliveryPackageList;
	}
	
	public List<DeliveryPackage> checkPackageDetailsAndAssignToAvailableVehicle(List<DeliveryPackage> masterDeliveryPackageList,
			List<DeliveryPackage> deliveredPackages, List<String> listOfDeliveredPckgIds) {
		
		Map<String, DeliveryPackage> mapOfPkgIdWithDeliveryPackage = new TreeMap<String, DeliveryPackage>();
		
		List<String> allCombos = new ArrayList<String>();
		DeliveryUtil.findAllCombos(masterDeliveryPackageList, 200,
				new ArrayList<DeliveryPackage>(),
				allCombos, mapOfPkgIdWithDeliveryPackage);
		
		String bestCombination = DeliveryUtil.sortAndGetBestPackageCombo(allCombos, mapOfPkgIdWithDeliveryPackage);
		
		List<DeliveryPackage> packagesToBeDelivered =
				DeliveryUtil.getDeliveryPckgFromBestCombination(bestCombination, mapOfPkgIdWithDeliveryPackage);
		
		Map<String, DeliveryVehicle> assignedVehicleWithPackages = DeliveryUtil.assignPackagetoVehicle
				(packagesToBeDelivered,
				masterDeliveryPackageList);
		
		// add delivery package to final delivery list
		DeliveryVehicle deliveryVehicle = null;
		if(deliveredPackages.size() > 0) {
			
			for(Map.Entry<String, DeliveryVehicle> entry: assignedVehicleWithPackages.entrySet()) {
				
				List<DeliveryPackage> dlvryPckgListInVehicle = entry.getValue().getDeliveryPackages();
				List<DeliveryPackage> newDlvryList = dlvryPckgListInVehicle
				.stream()
				.filter(pckg -> !listOfDeliveredPckgIds.contains(pckg.getPackageId()))
				.collect(Collectors.toList());
				
				if(!newDlvryList.isEmpty()) {
					deliveredPackages.addAll(newDlvryList);
					listOfDeliveredPckgIds.addAll(DeliveryUtil.addPackageIdsToList(newDlvryList, listOfDeliveredPckgIds));
				}
			}
		} else {
			deliveryVehicle = assignedVehicleWithPackages.entrySet().stream().findFirst().get().getValue();
			deliveredPackages.addAll(deliveryVehicle.getDeliveryPackages());
			listOfDeliveredPckgIds.addAll(DeliveryUtil.addPackageIdsToList(deliveryVehicle.getDeliveryPackages(), listOfDeliveredPckgIds));
		}
		
		logger.debug("Vehicle delivery details " + new Gson().toJson(assignedVehicleWithPackages));
		
		logger.debug("-----------------------END-OF-O/P---------------------------");
		
		return deliveredPackages;
	}
}
