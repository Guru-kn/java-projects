package com.everestengineering.delivery.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.everestengineering.delivery.constant.DeliveryConstant;
import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.model.PackageResponse;
import com.google.gson.Gson;

public class DeliveryUtil {

	Logger logger = Logger.getLogger(DeliveryUtil.class);

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
		packageResponse.setIndexOfMaxSum(Arrays.stream(indexesUsedToAdd).filter(x -> x >= 0).toArray());
		packageResponse.setMaxWeight(res);

		return packageResponse;
	}

	public static List<DeliveryPackage> readPackageDetails(List<String> packageList) {

		List<DeliveryPackage> deliveryPackageList = new ArrayList<DeliveryPackage>();

		DeliveryPackage deliveryPackage = null;
		for (String pckage : packageList) {

			deliveryPackage = new DeliveryPackage(pckage);
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

	public static void main(String[] args) {

		List<String> packageList = new ArrayList<String>();

		// pkg_id1 pkg_weight1_in_kg distance1_in_km offer_code1
		packageList.add("PKG1 50 30 OFR001");
		packageList.add("PKG2 75 125 OFFR0008");
		packageList.add("PKG3 175 100 OFFR003");
		packageList.add("PKG4 110 60 OFFR002");
		packageList.add("PKG5 155 95 NA");

		List<DeliveryPackage> deliveryPackageList = readPackageDetails(packageList);
		System.out.println(deliveryPackageList);
		
		Integer[] packageWeights = deliveryPackageList.stream().map(am -> am.getWeightInKg().intValue())
				.toArray(Integer[]::new);

		PackageResponse packageResponse = null;
		List<PackageResponse> packageWithMaxSumAndIndexOfMaxSumList = new ArrayList<PackageResponse>();
		for (int i = 0; i < packageWeights.length; i++) {
			packageResponse = findHeavierPackages(packageWeights, packageWeights.length, i);
			System.out.println(new Gson().toJson(packageResponse));
			packageWithMaxSumAndIndexOfMaxSumList.add(packageResponse);
		}

		Collections.sort(packageWithMaxSumAndIndexOfMaxSumList, new MaxWeightComparator());

		// String[] packageWithCostAndDiscount =
		// CostService.getInstance().calculateTotalCostOfDelivery("100 5");

		// 50, 75, 175, 110, 155 -> 50, 75, 110, 155, 175

		System.out.println(packageWithMaxSumAndIndexOfMaxSumList);
		
		// send packageWithMaxSumAndIndexOfMaxSumList to method and 
		// find if we have same maxweights, if yes check which has max packages
		// calculate distance when we have same number of packages and same weight
		// else pick max number of packages
		// else max weight package details
		
		double timeInHrs = calculateDeliveryTime(60);
	}
}
