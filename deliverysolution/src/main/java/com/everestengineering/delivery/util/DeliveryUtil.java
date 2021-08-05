package com.everestengineering.delivery.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.everestengineering.delivery.constant.DeliveryConstant;
import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.model.PackageResponse;
import com.everestengineering.discount.constant.CostConstants;
import com.everestengineering.discount.constant.DiscountConstant;
import com.everestengineering.discount.constant.ValidationConstant;
import com.everestengineering.discount.model.DeliveryVehicle;
import com.everestengineering.discount.model.DiscountResponse;
import com.everestengineering.discount.util.CostUtil;

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

		System.out.println(finalListWithMaxWeights);

		if (finalListWithMaxWeights.size() > 1) {

			// compare all the packages distance and then choose the package with nearest
			// distance
		}

		return finalListWithMaxWeights;
	}

	public static void assignPackagetoVehicle(PackageResponse packageWithMaxSumAndIndexOfMaxSumList,
			List<DeliveryPackage> masterPackageList) {

		// get available vehicle fleets
		Map<String, DeliveryVehicle> availableVehicleFleets = VehicleUtil.getAvailableVehicleFleets();
		logger.info("Available Vehicles: " + availableVehicleFleets);

		int[] arrOfPackagesWhichCanBeAssigned = packageWithMaxSumAndIndexOfMaxSumList.getIndexOfMaxSum();

		// get packages ready for delivery
		List<DeliveryPackage> listOfPckgRdyForDlvry = new ArrayList<DeliveryPackage>();
		DeliveryPackage packageReadyForDelivery = null;
		DiscountResponse discountResponse = null;
		double totalTimeOfDeliveryOfSingleTrip = 0;

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
				packageReadyForDelivery
						.setTimeTakenToDeliverInHrs(totTimeToDeliverPckge);
				totalTimeOfDeliveryOfSingleTrip = totalTimeOfDeliveryOfSingleTrip == 0 ?  (totalTimeOfDeliveryOfSingleTrip + totTimeToDeliverPckge) : Math.max(totalTimeOfDeliveryOfSingleTrip, totTimeToDeliverPckge);
				packageReadyForDelivery.setDiscountResponse(discountResponse);
				listOfPckgRdyForDlvry.add(packageReadyForDelivery);
			}
		}
		
		totalTimeOfDeliveryOfSingleTrip = totalTimeOfDeliveryOfSingleTrip * 2;

		Optional<Entry<String, DeliveryVehicle>> vehicleOption = availableVehicleFleets.entrySet().stream()
				.filter(vehicle -> vehicle.getValue().getIsAvailable()).findFirst();

		if (null != vehicleOption.get() && null != vehicleOption.get().getValue()) {
			DeliveryVehicle vehicleReadyToFlagOff = vehicleOption.get().getValue();
			vehicleReadyToFlagOff.setDeliveryPackages(listOfPckgRdyForDlvry);
			vehicleReadyToFlagOff.setNextAvailableInHrs(totalTimeOfDeliveryOfSingleTrip);
			VehicleUtil.setVehiclesInTransit(vehicleReadyToFlagOff);
			availableVehicleFleets.remove(vehicleReadyToFlagOff.getVId());
		}

		logger.info(availableVehicleFleets);
		logger.info(VehicleUtil.getVehiclesInTransit());
	}

	public static double getDoubleValueFromStringArr(String[] arr, int index) {
		return Double.valueOf(arr[index]);
	}

	public static void main(String[] args) {

		String baseCostToDeliveryNoOfPackages = "100 5";
		String[] baseCostToDlivrNoOfPckgArr = baseCostToDeliveryNoOfPackages.split(" ");

		List<String> packageList = new ArrayList<String>();

		// pkg_id1 pkg_weight1_in_kg distance1_in_km offer_code1
		packageList.add("PKG1 50 30 OFR001");
		packageList.add("PKG2 75 125 OFFR0008");
		packageList.add("PKG3 175 100 OFFR003");
		packageList.add("PKG4 110 60 OFFR002");
		packageList.add("PKG5 155 95 NA");

//		packageList.add("PKG1 75 30 OFR001");
//		packageList.add("PKG2 75 125 OFFR0008");
//		packageList.add("PKG3 50 100 OFFR003");
//		packageList.add("PKG4 75 60 OFFR002");
//		packageList.add("PKG5 25 95 NA");

		List<DeliveryPackage> masterDeliveryPackageList = readPackageDetails(packageList,
				getDoubleValueFromStringArr(baseCostToDlivrNoOfPckgArr, 0));
		System.out.println(masterDeliveryPackageList);

		Integer[] packageWeights = masterDeliveryPackageList.stream().map(am -> am.getWeightInKg().intValue())
				.toArray(Integer[]::new);

		PackageResponse packageResponse = null;
		List<PackageResponse> packageWithMaxSumAndIndexOfMaxSumList = new ArrayList<PackageResponse>();
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

		packageWithMaxSumAndIndexOfMaxSumList = checkAndSelectSuitablePackageToAssign(
				packageWithMaxSumAndIndexOfMaxSumList);

		System.out.println(packageWithMaxSumAndIndexOfMaxSumList);

		assignPackagetoVehicle(packageWithMaxSumAndIndexOfMaxSumList.get(0), masterDeliveryPackageList);

		double timeInHrs = calculateDeliveryTime(60);
	}
}
