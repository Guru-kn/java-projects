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

	private DeliveryUtil() {

	}

	public static DeliveryUtil deliveryUtil = null;

	static {
		if (deliveryUtil == null)
			deliveryUtil = new DeliveryUtil();
	}

	public static DeliveryUtil getInstance() {
		return deliveryUtil;
	}


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
			if (res >= 200 || (res + arr[i] > 200)) {
				break;
			}
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

	public List<DeliveryPackage> readPackageDetails(List<String> packageList, Double baseCostOfDelivery) {

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
		DeliveryVehicle vehicleAvlbleForDelivery = null;
		
		if(availableVehicleFleets.size() == 0) {
			logger.info("There are no available vehicles, need to check the next available from fleet");
			availableVehicleFleets = getNextAvailableVehicleInTransit(VehicleUtil.getVehiclesInTransit());
		}
		
		vehicleAvlbleForDelivery = availableVehicleFleets.entrySet().iterator().next().getValue();
		logger.info("Next available vehicle in the fleet is " +
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
				discountResponse = CostUtil.getInstance().calculateFinalDeliveryCost(
						packageReadyForDelivery.getBaseCostOfDelivery(), packageReadyForDelivery.getWeightInKg(),
						packageReadyForDelivery.getDistanceInKms(),
						packageReadyForDelivery.getDiscountCoupon() != null
								? packageReadyForDelivery.getDiscountCoupon().getCouponCode()
								: ValidationConstant.INVALID_INPUT);
				
				double totTimeToDeliverPckge = calculateDeliveryTime(packageReadyForDelivery.getDistanceInKms());
				
				packageReadyForDelivery.
				setTimeTakenToDeliverInHrs(
						getDoubleValueRoundedToTwo(vehicleAvlbleForDelivery.getNextAvailableInHrs() +
								totTimeToDeliverPckge));
				
				totalTimeOfDeliveryOfPckg = totalTimeOfDeliveryOfPckg == 0
						? (totalTimeOfDeliveryOfPckg + totTimeToDeliverPckge)
						: Math.max(totalTimeOfDeliveryOfPckg, totTimeToDeliverPckge);
						
				packageReadyForDelivery.setDiscountResponse(discountResponse);
				listOfPckgRdyForDlvry.add(packageReadyForDelivery);
			}
		}

		totalTimeOfDeliveryOfPckg = vehicleAvlbleForDelivery.getNextAvailableInHrs() + (totalTimeOfDeliveryOfPckg * 2);

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

	public double getDoubleValueFromStringArr(String[] arr, int index) {
		return Double.valueOf(arr[index]);
	}
	
	public static Double getDoubleValueRoundedToTwo(Double val) {
		
		return new BigDecimal(val).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	public List<DeliveryPackage> checkPackageDetailsAndAssignToAvailableVehicle(List<DeliveryPackage> masterDeliveryPackageList, List<DeliveryPackage> deliveredPackages, List<String> listOfDeliveredPckgIds) {

		Integer[] packageWeights = masterDeliveryPackageList.stream().map(am -> am.getWeightInKg().intValue())
				.toArray(Integer[]::new);

		PackageResponse packageResponse = null;
		List<PackageResponse> packageWithMaxSumAndIndexOfMaxSumList = new ArrayList<PackageResponse>();
		List<PackageResponse> packagesToBeDelivered = null;
		
		for (int i = 0; i < packageWeights.length; i++) {
			packageResponse = findHeavierPackages(packageWeights, packageWeights.length, i);
			
			System.out.println("after findHeavierPackages " + new Gson().toJson(packageResponse));
			
			packageWithMaxSumAndIndexOfMaxSumList.add(packageResponse);
		}
		
		//Sort pckgs with max weight first
		Collections.sort(packageWithMaxSumAndIndexOfMaxSumList, Comparator.comparing(PackageResponse::getMaxWeight)
				.reversed().thenComparing(PackageResponse::getNumberOfPackages));

		packagesToBeDelivered = checkAndSelectSuitablePackageToAssign(packageWithMaxSumAndIndexOfMaxSumList);

		Map<String, DeliveryVehicle> assignedVehicleWithPackages = assignPackagetoVehicle(packagesToBeDelivered.get(0),
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
					listOfDeliveredPckgIds.addAll(addPackageIdsToList(newDlvryList, listOfDeliveredPckgIds));
				}
			}
		} else {
			deliveryVehicle = assignedVehicleWithPackages.entrySet().stream().findFirst().get().getValue();
			deliveredPackages.addAll(deliveryVehicle.getDeliveryPackages());
			listOfDeliveredPckgIds.addAll(addPackageIdsToList(deliveryVehicle.getDeliveryPackages(), listOfDeliveredPckgIds));
		}
		
		logger.info("Vehicle delivery details " + new Gson().toJson(assignedVehicleWithPackages));
		
		logger.info("-----------------------END-OF-O/P---------------------------");
		
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
}
