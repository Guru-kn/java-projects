package com.everestengineering.delivery.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeliveryUtil {

	public static int findHeavierPackages(Integer arr[], int arrLength, int maxNumOfEleToSumWith) {
		int[] indexesUsedToAdd = new int[arr.length];
		// k must be greater
		if (arrLength < maxNumOfEleToSumWith) {
			return -1;
		}

		int res = 0;
		for (int i = 0; i < maxNumOfEleToSumWith; i++) {
			if (res >= 200 || (res + arr[i] > 200))
				break;
			res += arr[i];
			indexesUsedToAdd[i] = i + 1;
		}

		int curr_sum = res;
		System.out.println("curr_sum " + curr_sum);
		for (int i = maxNumOfEleToSumWith; i < arrLength; i++) {
			if (res <= 200 && ((curr_sum + arr[i] - arr[i - maxNumOfEleToSumWith]) <= 200)) {
				curr_sum += arr[i] - arr[i - maxNumOfEleToSumWith];
				indexesUsedToAdd[i - maxNumOfEleToSumWith] = i + 1;
			}
			res = Math.max(res, curr_sum);
		}

		return res;
	}

	public static void main(String[] args) {

		List<String> packageList = new ArrayList<String>();

		// pkg_id1 pkg_weight1_in_kg distance1_in_km offer_code1
		packageList.add("PKG1 50 30 OFR001");
		packageList.add("PKG2 75 125 OFFR0008");
		packageList.add("PKG3 175 100 OFFR003");
		packageList.add("PKG4 110 60 OFFR002");
		packageList.add("PKG5 155 95 NA");

		Integer[] arr1 = { 75, 75, 75, 50, 25 };
		Integer[] arr2 = { 175, 155, 110, 75, 50 };

		Arrays.sort(arr1);
		
		// 25, 50, 75, 75, 75
		System.out.println(findHeavierPackages(arr1, arr1.length, 3));
		
		// 50, 75, 110, 155, 175
		Arrays.sort(arr2);
		System.out.println(findHeavierPackages(arr2, arr2.length, 2));
	}
}
