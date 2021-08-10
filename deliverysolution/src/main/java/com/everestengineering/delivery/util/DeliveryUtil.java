package com.everestengineering.delivery.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.everestengineering.delivery.constant.DeliveryConstant;
import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.model.PackageResponse;

public class DeliveryUtil {

	static Logger logger = Logger.getLogger(DeliveryUtil.class);

	private DeliveryUtil() {

	}

	public static PackageResponse findHeavierPackages(Integer[] packageWeights, int arrLength, int maxNumOfEleToSumWith) {
		int[] indexesUsedToAdd = new int[packageWeights.length];
		Arrays.fill(indexesUsedToAdd, -1);

		// k must be greater
		if (arrLength < maxNumOfEleToSumWith) {
			PackageResponse packageResponse = new PackageResponse();
			packageResponse.setIndexOfMaxSum(Arrays.stream(indexesUsedToAdd).filter(x -> x >= 0).toArray());
			packageResponse.setMaxWeight(0);
			return packageResponse;
		}

		int maxWeight = 0;
		
		int lastIndexAdded = -1;
		for (int i = 0; i < maxNumOfEleToSumWith; i++) {
			if (maxWeight >= DeliveryConstant.MAX_WEIGHT_LOAD_IN_KGS
					|| (maxWeight + packageWeights[i] > DeliveryConstant.MAX_WEIGHT_LOAD_IN_KGS)) {
				break;
			}
			maxWeight += packageWeights[i];
			indexesUsedToAdd[i] = i;
			lastIndexAdded = i;
		}

		int currSum = maxWeight;
		for (int i = maxNumOfEleToSumWith; i < arrLength; i++) {
			if (maxWeight < DeliveryConstant.MAX_WEIGHT_LOAD_IN_KGS &&
					((currSum + packageWeights[i] - packageWeights[i - maxNumOfEleToSumWith]) <=
					DeliveryConstant.MAX_WEIGHT_LOAD_IN_KGS)) {
				currSum += packageWeights[i] - packageWeights[i - maxNumOfEleToSumWith];
				indexesUsedToAdd[i - maxNumOfEleToSumWith] = i;
			} else if(maxWeight < DeliveryConstant.MAX_WEIGHT_LOAD_IN_KGS &&
					(currSum - packageWeights[lastIndexAdded] + packageWeights[i] <= DeliveryConstant.MAX_WEIGHT_LOAD_IN_KGS)) {
				currSum = currSum - packageWeights[lastIndexAdded] + packageWeights[i];
				indexesUsedToAdd[i] = i;
				indexesUsedToAdd[maxNumOfEleToSumWith-1] = -1;
			}
			
			maxWeight = Math.max(maxWeight, currSum);
		}
		
		indexesUsedToAdd = Arrays.stream(indexesUsedToAdd).filter(x -> x >= 0).toArray();

		PackageResponse packageResponse = new 
				PackageResponse(maxWeight, indexesUsedToAdd, indexesUsedToAdd.length);

		return packageResponse;
	}

	public static double calculateDeliveryTime(double distance) {

		Double timeInHrs = distance / DeliveryConstant.MAX_SPEED_PER_HR_IN_KMS;
		BigDecimal bd = BigDecimal.valueOf(timeInHrs);
		bd = bd.setScale(2, RoundingMode.FLOOR);
		return bd.doubleValue();
	}

	public static double getDoubleValueFromStringArr(String[] arr, int index) {
		return Double.valueOf(arr[index]);
	}
	
	public static double getDoubleValueFromString(String str) {
		return Double.valueOf(str);
	}
	
	public static Double getDoubleValueRoundedToTwo(Double val) {
		
		return new BigDecimal(val).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
	public static List<String> addPackageIdsToList(List<DeliveryPackage> deliveryPckgs,
			List<String> listOfDeliveredPckgIds) {
		List<String> dlPckgIds = deliveryPckgs
				.stream()
				.map(DeliveryPackage::getPackageId)
				.collect(Collectors.toList());
		return dlPckgIds;
	}
	
	public static List<String> deliveryPackageListToExpectedFormat(List<DeliveryPackage> deliveryPackageList){
		
		List<String> deliveredPckgs = deliveryPackageList.stream()
				.map(pckg -> 
				new DeliveryPackage(
				pckg.getPackageId(), pckg.getDiscountResponse().getTotalDiscountInAmount(),
				pckg.getDiscountResponse().getFinalDeliveryCost(), pckg.getTimeTakenToDeliverInHrs()).getDeliveredPckgDetails())
				.collect(Collectors.toList());
		Collections.sort(deliveredPckgs);
		return deliveredPckgs;
	}
}
