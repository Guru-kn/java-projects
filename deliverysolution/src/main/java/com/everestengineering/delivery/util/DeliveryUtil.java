package com.everestengineering.delivery.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.everestengineering.delivery.constant.DeliveryConstant;
import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.model.PackageResponse;
import com.everestengineering.discount.constant.ValidationConstant;
import com.everestengineering.discount.model.DeliveryVehicle;
import com.everestengineering.discount.model.DiscountResponse;
import com.everestengineering.discount.util.CostUtil;
import com.google.gson.Gson;

public class DeliveryUtil {

	static Logger logger = Logger.getLogger(DeliveryUtil.class);

	public static PackageResponse findHeavierPackages(Integer arr[], int arrLength, int maxNumOfEleToSumWith) {
		int[] indexesUsedToAdd = new int[arr.length];
		Arrays.fill(indexesUsedToAdd, -1);

		// k must be greater
		if (arrLength < maxNumOfEleToSumWith) {
			PackageResponse packageResponse = new PackageResponse();
			packageResponse.setIndexOfMaxSum(Arrays.stream(indexesUsedToAdd).filter(x -> x >= 0).toArray());
			packageResponse.setMaxWeight(0);
			return packageResponse;
		}

		int res = 0;
		for (int i = 0; i < maxNumOfEleToSumWith; i++) {
			if (res >= 200 || (res + arr[i] > 200))
				break;
			res += arr[i];
			indexesUsedToAdd[i] = i;
		}

		int curr_sum = res;
		for (int i = maxNumOfEleToSumWith; i < arrLength; i++) {
			if (res <= 200 && ((curr_sum + arr[i] - arr[i - maxNumOfEleToSumWith]) <= 200)) {
				curr_sum += arr[i] - arr[i - maxNumOfEleToSumWith];
				indexesUsedToAdd[i - maxNumOfEleToSumWith] = i;
			}
			res = Math.max(res, curr_sum);
		}

		PackageResponse packageResponse = new PackageResponse();
		indexesUsedToAdd = Arrays.stream(indexesUsedToAdd).filter(x -> x >= 0).toArray();
		packageResponse.setIndexOfMaxSum(indexesUsedToAdd);
		packageResponse.setNumberOfPackages(indexesUsedToAdd.length);
		packageResponse.setMaxWeight(res);

		return packageResponse;
	}

	public static List<DeliveryPackage> readPackageDetails(List<String> packageList, Double baseCostOfDelivery) {

		List<DeliveryPackage> deliveryPackageList = new ArrayList<DeliveryPackage>();

		DeliveryPackage deliveryPackage = null;
		for (String pckage : packageList) {

			deliveryPackage = new DeliveryPackage(pckage, baseCostOfDelivery);
			deliveryPackageList.add(deliveryPackage);
		}

		// 50, 75, 175, 110, 155 -> 50, 75, 110, 155, 175
		Collections.sort(deliveryPackageList, new PackageWeightComparator());
		return deliveryPackageList;
	}

	public static double calculateDeliveryTime(double distance) {

		Double timeInHrs = distance / DeliveryConstant.MAX_SPEED_PER_HR_IN_KMS;
		BigDecimal bd = BigDecimal.valueOf(timeInHrs);
		bd = bd.setScale(2, RoundingMode.FLOOR);
		return bd.doubleValue();
	}

	public static List<PackageResponse> checkAndSelectSuitablePackageToAssign(
			List<PackageResponse> packageWithMaxSumAndIndexOfMaxSumList) {

		List<PackageResponse> finalListWithMaxWeights = packageWithMaxSumAndIndexOfMaxSumList.stream()
				.filter(o -> o.getMaxWeight().equals(packageWithMaxSumAndIndexOfMaxSumList.get(0).getMaxWeight()))
				.collect(Collectors.toList());

		if (finalListWithMaxWeights.size() > 1) {

			// compare all the packages distance and then choose the package with nearest
			// distance
		}

		return finalListWithMaxWeights;
	}
	
	public static Map<String, DeliveryVehicle> getNextAvailableVehicleInTransit(Map<String, DeliveryVehicle> vehiclesInTransit) {
		
		Map<String, Double> mapOfVhcleAndNxtAvalbleTime = vehiclesInTransit.entrySet()
		        .stream()
		        .collect(Collectors.toMap(Map.Entry::getKey,
		                                  e -> new Double(e.getValue().getNextAvailableInHrs())));
		
		Map<String, Double> sortedByCount = mapOfVhcleAndNxtAvalbleTime.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Double>comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
		System.out.println("sortedByCount " + sortedByCount);
		
		Map<String, DeliveryVehicle> newMap = vehiclesInTransit.entrySet()
		.stream()
		.filter(x -> x.getKey().equals(sortedByCount.entrySet().iterator().next().getKey()))
		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
		return newMap;
	}

	public static Map<String, DeliveryVehicle> assignPackagetoVehicle(
			PackageResponse packageWithMaxSumAndIndexOfMaxSumList, List<DeliveryPackage> masterPackageList) {

		// get available vehicle fleets
		Map<String, DeliveryVehicle> availableVehicleFleets = VehicleUtil.getAvailableVehicleFleets();
		
		if(availableVehicleFleets.size() == 0) {
			availableVehicleFleets = getNextAvailableVehicleInTransit(VehicleUtil.getVehiclesInTransit());
			logger.info("availableVehicleFleets " + new Gson().toJson(availableVehicleFleets));
		}

		int[] arrOfPackagesWhichCanBeAssigned = packageWithMaxSumAndIndexOfMaxSumList.getIndexOfMaxSum();
		
		DeliveryVehicle vehicleAvlbleForDelivery = availableVehicleFleets.entrySet().iterator().next().getValue();
		// get packages ready for delivery
		List<DeliveryPackage> listOfPckgRdyForDlvry = vehicleAvlbleForDelivery.getDeliveryPackages();
		
		if(null == listOfPckgRdyForDlvry) {
			listOfPckgRdyForDlvry = new ArrayList<DeliveryPackage>();
		}
		
		DeliveryPackage packageReadyForDelivery = null;
		DiscountResponse discountResponse = null;
		double totalTimeOfDeliveryOfPckg = 0;

		for (int indxInMasterList : arrOfPackagesWhichCanBeAssigned) {
			packageReadyForDelivery = masterPackageList.get(indxInMasterList);

			if (null != packageReadyForDelivery) {
				discountResponse = CostUtil.getInstance().calculateFinalDeliveryCost(
						packageReadyForDelivery.getBaseCostOfDelivery(), packageReadyForDelivery.getWeightInKg(),
						packageReadyForDelivery.getDistanceInKms(),
						packageReadyForDelivery.getDiscountCoupon() != null
								? packageReadyForDelivery.getDiscountCoupon().getCouponCode()
								: ValidationConstant.INVALID_INPUT);
				
				double totTimeToDeliverPckge = calculateDeliveryTime(packageReadyForDelivery.getDistanceInKms());
				
				packageReadyForDelivery.setTimeTakenToDeliverInHrs(vehicleAvlbleForDelivery.getNextAvailableInHrs() + totTimeToDeliverPckge);
				
				totalTimeOfDeliveryOfPckg = totalTimeOfDeliveryOfPckg == 0
						? (totalTimeOfDeliveryOfPckg + totTimeToDeliverPckge)
						: Math.max(totalTimeOfDeliveryOfPckg, totTimeToDeliverPckge);
				packageReadyForDelivery.setDiscountResponse(discountResponse);
				listOfPckgRdyForDlvry.add(packageReadyForDelivery);
			}
		}

		totalTimeOfDeliveryOfPckg = vehicleAvlbleForDelivery.getNextAvailableInHrs() + totalTimeOfDeliveryOfPckg * 2;

		Optional<Entry<String, DeliveryVehicle>> vehicleOption = availableVehicleFleets.entrySet().stream()
				.filter(vehicle -> vehicle.getValue().getIsAvailable()).findFirst();

		if (null != vehicleOption.get() && null != vehicleOption.get().getValue()) {
			DeliveryVehicle vehicleReadyToFlagOff = vehicleOption.get().getValue();
			vehicleReadyToFlagOff.setDeliveryPackages(listOfPckgRdyForDlvry);
			vehicleReadyToFlagOff.setNextAvailableInHrs(totalTimeOfDeliveryOfPckg);
			VehicleUtil.setVehiclesInTransit(vehicleReadyToFlagOff);
			availableVehicleFleets.remove(vehicleReadyToFlagOff.getVId());
		}

		// logger.info(availableVehicleFleets);
		// logger.info(new Gson().toJson(VehicleUtil.getVehiclesInTransit()));

		return VehicleUtil.getVehiclesInTransit();
	}

	public static double getDoubleValueFromStringArr(String[] arr, int index) {
		return Double.valueOf(arr[index]);
	}

	public static List<DeliveryPackage> checkPacakageDetailsAndAssignToAvailableVehicle(List<DeliveryPackage> masterDeliveryPackageList, List<DeliveryPackage> deliveredPackages, List<String> listOfDeliveredPckgIds) {

		Integer[] packageWeights = masterDeliveryPackageList.stream().map(am -> am.getWeightInKg().intValue())
				.toArray(Integer[]::new);

		PackageResponse packageResponse = null;
		List<PackageResponse> packageWithMaxSumAndIndexOfMaxSumList = new ArrayList<PackageResponse>();
		List<PackageResponse> packagesToBeDelivered = null;
		
		for (int i = 0; i < packageWeights.length; i++) {
			packageResponse = findHeavierPackages(packageWeights, packageWeights.length, i);
			packageWithMaxSumAndIndexOfMaxSumList.add(packageResponse);
		}

		// Collections.sort(packageWithMaxSumAndIndexOfMaxSumList, new
		// MaxWeightComparator());

		// String[] packageWithCostAndDiscount =
		// CostService.getInstance().calculateTotalCostOfDelivery("100 5");

		Collections.sort(packageWithMaxSumAndIndexOfMaxSumList, Comparator.comparing(PackageResponse::getMaxWeight)
				.reversed().thenComparing(PackageResponse::getNumberOfPackages));

		// 50, 75, 175, 110, 155 -> 50, 75, 110, 155, 175
		// send packageWithMaxSumAndIndexOfMaxSumList to method and
		// find if we have same maxweights, if yes check which has max packages
		// calculate distance when we have same number of packages and same weight
		// else pick max number of packages
		// else max weight package details

		packagesToBeDelivered = checkAndSelectSuitablePackageToAssign(packageWithMaxSumAndIndexOfMaxSumList);

		System.out.println("packagesToBeDelivered " + packagesToBeDelivered);

		Map<String, DeliveryVehicle> assignedVehicleWithPackages = assignPackagetoVehicle(packagesToBeDelivered.get(0),
				masterDeliveryPackageList);
		
		System.out.println("assignedVehicleWithPackages " + new Gson().toJson(assignedVehicleWithPackages));
		
		DeliveryVehicle deliveryVehicle = null;
		if(deliveredPackages.size() > 0) {
//			List<List<DeliveryPackage>> newPckgList = assignedVehicleWithPackages.entrySet().
//			stream().map(v -> v.getValue())
//			.map(v -> v.getDeliveryPackages())
//			.map(pckgList -> pckgList.stream().
//					filter(pckg -> !listOfDeliveredPckgIds.contains(pckg.getPackageId())).collect(Collectors.toList())).filter(lst -> !lst.isEmpty()).collect(Collectors.toList());
			
			for(Map.Entry<String, DeliveryVehicle> entry: assignedVehicleWithPackages.entrySet()) {
				
				List<DeliveryPackage> dlvryPckgListInVehicle = entry.getValue().getDeliveryPackages();
				List<DeliveryPackage> newDlvryList = dlvryPckgListInVehicle
				.stream()
				.filter(pckg -> !listOfDeliveredPckgIds.contains(pckg.getPackageId()))
				.collect(Collectors.toList());
				
				if(!newDlvryList.isEmpty()) {
					deliveredPackages.addAll(newDlvryList);
					listOfDeliveredPckgIds.addAll(addPackageIdsToList(newDlvryList, listOfDeliveredPckgIds));
				}
			}
		} else {
			deliveryVehicle = assignedVehicleWithPackages.entrySet().stream().findFirst().get().getValue();
			deliveredPackages.addAll(deliveryVehicle.getDeliveryPackages());
			listOfDeliveredPckgIds.addAll(addPackageIdsToList(deliveryVehicle.getDeliveryPackages(), listOfDeliveredPckgIds));
		}
		
		return deliveredPackages;
	}
	
	public static List<String> addPackageIdsToList(List<DeliveryPackage> deliveryPckgs,
			List<String> listOfDeliveredPckgIds) {
		List<String> dlPckgIds = deliveryPckgs
				.stream()
				.map(DeliveryPackage::getPackageId)
				.collect(Collectors.toList());
		return dlPckgIds;
	}

	public static void main(String[] args) {

		String baseCostToDeliveryNoOfPackages = "100 5";
		String[] baseCostToDlivrNoOfPckgArr = baseCostToDeliveryNoOfPackages.split(" ");

		List<String> packageList = new ArrayList<String>();

		// pkg_id1 pkg_weight1_in_kg distance1_in_km offer_code1
//		packageList.add("PKG1 50 30 OFR001");
//		packageList.add("PKG2 75 125 OFR0008");
//		packageList.add("PKG3 175 100 OFR003");
//		packageList.add("PKG4 110 60 OFR002");
//		packageList.add("PKG5 155 95 NA");

		packageList.add("PKG1 75 30 OFR001");
		packageList.add("PKG2 75 125 OFFR0008");
		packageList.add("PKG3 50 100 OFFR003");
		packageList.add("PKG4 75 60 OFFR002");
		packageList.add("PKG5 25 95 NA");

		List<DeliveryPackage> masterDeliveryPackageList = readPackageDetails(packageList,
				getDoubleValueFromStringArr(baseCostToDlivrNoOfPckgArr, 0));
		
		List<DeliveryPackage> deliveredPackages = new ArrayList<DeliveryPackage>();
		List<String> listOfDeliveredPckgIds = new ArrayList<String>();
		
		while(masterDeliveryPackageList.size() > 0) {
			
			deliveredPackages = checkPacakageDetailsAndAssignToAvailableVehicle(masterDeliveryPackageList, deliveredPackages, listOfDeliveredPckgIds);
			
			System.out.println("deliveredPackages " + new Gson().toJson(deliveredPackages));
			
			// removing the delivered item from package list to deliver
			if(deliveredPackages.size() > 0) {
				List<String> valuesToCheck = deliveredPackages.stream().map(DeliveryPackage::getPackageId).collect(Collectors.toList());
				masterDeliveryPackageList = masterDeliveryPackageList.stream().
				filter(x->!valuesToCheck.contains(x.getPackageId())).
				collect(Collectors.toList());
				
				System.out.println("After delivery ");
				System.out.println(new Gson().toJson(masterDeliveryPackageList));
			}
		}
	}
}
