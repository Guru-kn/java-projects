package com.everestengineering.delivery.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.model.PackageResponse;
import com.everestengineering.delivery.util.DeliveryUtil;
import com.everestengineering.delivery.util.PackageWeightComparator;
import com.everestengineering.delivery.util.VehicleUtil;
import com.everestengineering.discount.constant.ValidationConstant;
import com.everestengineering.discount.model.DeliveryVehicle;
import com.everestengineering.discount.model.DiscountResponse;
import com.everestengineering.discount.model.OrderResponse;
import com.everestengineering.discount.service.CostService;
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
	
	public static List<PackageResponse> checkAndSelectSuitablePackageToAssign(
			List<PackageResponse> packageWithMaxSumAndIndexOfMaxSumList) {

		List<PackageResponse> finalListWithMaxWeights = packageWithMaxSumAndIndexOfMaxSumList.stream()
				.filter(o -> o.getMaxWeight().equals(packageWithMaxSumAndIndexOfMaxSumList.get(0).getMaxWeight()))
				.collect(Collectors.toList());

		return finalListWithMaxWeights;
	}
	
	

	
	public Map<String, DeliveryVehicle> assignPackagetoVehicleOld(
			PackageResponse packageWithMaxSumAndIndexOfMaxSumList, List<DeliveryPackage> masterPackageList) {

		// get available vehicle fleets
		Map<String, DeliveryVehicle> availableVehicleFleets = VehicleUtil.getAvailableVehicleFleets();
		DeliveryVehicle vehicleAvlbleForDelivery = null;
		
		if(availableVehicleFleets.size() == 0) {
			logger.debug("There are no available vehicles, need to check the next available from fleet");
			availableVehicleFleets = DeliveryUtil.getNextAvailableVehicleInTransit(VehicleUtil.getVehiclesInTransit());
		}
		
		vehicleAvlbleForDelivery = availableVehicleFleets.entrySet().iterator().next().getValue();
		logger.debug("Next available vehicle in the fleet is " +
				availableVehicleFleets.entrySet().iterator().next().getValue().getVId() + " after " +
				vehicleAvlbleForDelivery.getNextAvailableInHrs() + " hrs");

		int[] arrOfPackagesWhichCanBeAssigned = packageWithMaxSumAndIndexOfMaxSumList.getIndexOfMaxSum();
		
		// get packages ready for delivery
		List<DeliveryPackage> listOfPckgRdyForDlvry = vehicleAvlbleForDelivery.getDeliveryPackages();
		
		if(null == listOfPckgRdyForDlvry) {
			listOfPckgRdyForDlvry = new ArrayList<DeliveryPackage>();
		}
		
		DeliveryPackage packageReadyForDelivery = null;
		DiscountResponse discountResponse = null;
		double totalTimeOfDeliveryOfPckg = 0;
		
		// adding all the details required for package to be delivered ie cost, discount, time required ...
		for (int indxInMasterList : arrOfPackagesWhichCanBeAssigned) {
			packageReadyForDelivery = masterPackageList.get(indxInMasterList);

			if (null != packageReadyForDelivery) {
				discountResponse = CostService.getInstance().calculateFinalDeliveryCost(
						packageReadyForDelivery.getBaseCostOfDelivery(), packageReadyForDelivery.getWeightInKg(),
						packageReadyForDelivery.getDistanceInKms(),
						packageReadyForDelivery.getDiscountCoupon() != null
								? packageReadyForDelivery.getDiscountCoupon().getCouponCode()
								: ValidationConstant.INVALID_INPUT);
				
				double totTimeToDeliverPckge = DeliveryUtil.calculateDeliveryTime(packageReadyForDelivery.getDistanceInKms());
				
				packageReadyForDelivery.
				setTimeTakenToDeliverInHrs(
						DeliveryUtil.getDoubleValueRoundedToTwo(vehicleAvlbleForDelivery.getNextAvailableInHrs() +
								totTimeToDeliverPckge));
				
				totalTimeOfDeliveryOfPckg = totalTimeOfDeliveryOfPckg == 0
						? (totalTimeOfDeliveryOfPckg + totTimeToDeliverPckge)
						: Math.max(totalTimeOfDeliveryOfPckg, totTimeToDeliverPckge);
						
				packageReadyForDelivery.setDiscountResponse(discountResponse);
				listOfPckgRdyForDlvry.add(packageReadyForDelivery);
			}
		}
		
		// next available time plus (total tim for delivery * 2) -> *2 because its round trip
		totalTimeOfDeliveryOfPckg = vehicleAvlbleForDelivery.getNextAvailableInHrs() +
				(totalTimeOfDeliveryOfPckg * 2);

		Optional<Entry<String, DeliveryVehicle>> vehicleOption = availableVehicleFleets.entrySet().stream()
				.filter(vehicle -> vehicle.getValue().getIsAvailable()).findFirst();
		
		// updating vehicle fleet after assiging package to vehicle
		if (null != vehicleOption.get() && null != vehicleOption.get().getValue()) {
			DeliveryVehicle vehicleReadyToFlagOff = vehicleOption.get().getValue();
			vehicleReadyToFlagOff.setDeliveryPackages(listOfPckgRdyForDlvry);
			vehicleReadyToFlagOff.setNextAvailableInHrs(totalTimeOfDeliveryOfPckg);
			VehicleUtil.setVehiclesInTransit(vehicleReadyToFlagOff);
			availableVehicleFleets.remove(vehicleReadyToFlagOff.getVId());
		}
		return VehicleUtil.getVehiclesInTransit();
	}
	
	public List<DeliveryPackage> checkPackageDetailsAndAssignToAvailableVehicleOld(List<DeliveryPackage> masterDeliveryPackageList, List<DeliveryPackage> deliveredPackages, List<String> listOfDeliveredPckgIds) {

		Integer[] packageWeights = masterDeliveryPackageList.stream().map(am -> am.getWeightInKg().intValue())
				.toArray(Integer[]::new);
		
		Integer[] packageDistance = masterDeliveryPackageList.stream().map(am -> am.getDistanceInKms().intValue())
				.toArray(Integer[]::new);

		PackageResponse packageResponse = null;
		List<PackageResponse> packageWithMaxSumAndIndexOfMaxSumList = new ArrayList<PackageResponse>();
		List<PackageResponse> packagesToBeDelivered = null;
		
		for (int i = 0; i < packageWeights.length; i++) {
			packageResponse = DeliveryUtil.findHeavierPackages(packageWeights, packageWeights.length, i, packageDistance);
			
			if(i == 3) {
				System.out.println("after findHeavierPackages " + new Gson().toJson(packageResponse));
			}
			
			if(null != packageResponse) {
				packageWithMaxSumAndIndexOfMaxSumList.add(packageResponse);
			}
		}
		
		//Sort pckgs with max weight first
		Collections.sort(packageWithMaxSumAndIndexOfMaxSumList, 
				Comparator.comparing(PackageResponse::getNumberOfPackages)
				.reversed().thenComparing(PackageResponse::getMaxWeight).reversed()
				.thenComparing(PackageResponse::getTotalDistance));
		
		System.out.println("packageWithMaxSumAndIndexOfMaxSumList " + packageWithMaxSumAndIndexOfMaxSumList);

		packagesToBeDelivered = checkAndSelectSuitablePackageToAssign(packageWithMaxSumAndIndexOfMaxSumList);

		Map<String, DeliveryVehicle> assignedVehicleWithPackages = assignPackagetoVehicleOld(packagesToBeDelivered.get(0),
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
	
	public List<DeliveryPackage> checkPackageDetailsAndAssignToAvailableVehicle(List<DeliveryPackage> masterDeliveryPackageList,
			List<DeliveryPackage> deliveredPackages, List<String> listOfDeliveredPckgIds) {
		
		Map<String, DeliveryPackage> mapOfPkgIdWithDeliveryPackage = new TreeMap<String, DeliveryPackage>();
		
		List<String> allCombos = new ArrayList<String>();
		DeliveryUtil.findAllCombos(masterDeliveryPackageList, 200,
				new ArrayList<DeliveryPackage>(),
				allCombos, mapOfPkgIdWithDeliveryPackage);
		
		List<DeliveryPackage> packagesToBeDelivered = DeliveryUtil.sortAndGetBestPackageCombo(allCombos, mapOfPkgIdWithDeliveryPackage);
		
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
