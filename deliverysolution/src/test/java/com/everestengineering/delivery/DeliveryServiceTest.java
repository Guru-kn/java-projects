package com.everestengineering.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.model.PackageResponse;
import com.everestengineering.delivery.service.DeliveryService;
import com.everestengineering.delivery.util.DeliveryUtil;

public class DeliveryServiceTest {
	
	Logger logger = Logger.getLogger(DeliveryServiceTest.class);
	
	@Test
	public void testMaxWeightIndexCalculation() {
		
		Integer[] packageWeights = {50, 75, 110, 155, 175};
		Integer[] packageDistance = {50, 15, 5, 10, 25};
		
		PackageResponse packageResponse = DeliveryUtil.findHeavierPackages(packageWeights,
				packageWeights.length, 2, packageDistance);
		
		int[] expected = {2, 1};
		for(int i=0; i< expected.length; i++) {
			assertEquals(expected[i], packageResponse.getIndexOfMaxSum()[i]);
		}
		assertEquals(185, packageResponse.getMaxWeight());
		
		packageWeights = new Integer[]{50, 75, 150};
		packageDistance = new Integer[]{50, 15, 5, 10, 25};
		
		packageResponse = DeliveryUtil.findHeavierPackages(packageWeights,
				packageWeights.length, 2, packageDistance);
		
		expected = new int[]{0, 2};
		for(int i=0; i< expected.length; i++) {
			assertEquals(expected[i], packageResponse.getIndexOfMaxSum()[i]);
		}
		assertEquals(200, packageResponse.getMaxWeight());
		
		packageWeights = new Integer[]{50, 50, 150};
		packageDistance = new Integer[]{50, 15, 5, 10, 25};
		
		packageResponse = DeliveryUtil.findHeavierPackages(packageWeights,
				packageWeights.length, 2, packageDistance);
		
		expected = new int[]{2, 1};
		for(int i=0; i< expected.length; i++) {
			assertEquals(expected[i], packageResponse.getIndexOfMaxSum()[i]);
		}
		assertEquals(200, packageResponse.getMaxWeight());
		
		packageWeights = new Integer[]{50, 175};
		packageDistance = new Integer[]{50, 15, 5, 10, 25};
		
		packageResponse = DeliveryUtil.findHeavierPackages(packageWeights,
				packageWeights.length, 1, packageDistance);
		
		expected = new int[]{1};
		for(int i=0; i< expected.length; i++) {
			assertEquals(expected[i], packageResponse.getIndexOfMaxSum()[i]);
		}
		assertEquals(175, packageResponse.getMaxWeight());
		
		packageWeights = new Integer[]{50, 75, 150, 175};
		packageDistance = new Integer[]{50, 15, 5, 10, 25};
		
		packageResponse = DeliveryUtil.findHeavierPackages(packageWeights,
				packageWeights.length, 2, packageDistance);
		
		expected = new int[]{0, 2};
		for(int i=0; i< expected.length; i++) {
			assertEquals(expected[i], packageResponse.getIndexOfMaxSum()[i]);
		}
		assertEquals(200, packageResponse.getMaxWeight());
		
		packageWeights = new Integer[]{50, 75, 150, 200};
		packageDistance = new Integer[]{50, 15, 5, 10, 25};
		
		packageResponse = DeliveryUtil.findHeavierPackages(packageWeights,
				packageWeights.length, 1, packageDistance);
		
		expected = new int[]{1};
		for(int i=0; i< expected.length; i++) {
			assertEquals(expected[i], packageResponse.getIndexOfMaxSum()[i]);
		}
		assertEquals(200, packageResponse.getMaxWeight());
	}

	@Test
	public void testDeliveryService() {
		
//		List<String> list = new ArrayList<String>();
//		list.add("PKG1 75 5 OFR001");
//		list.add("PKG2 150 5 OFR002");
//		list.add("PKG3 50 100 OFR003");
//		
//		List<DeliveryPackage> deliveryPackageList = DeliveryService.getInstance().
//				sendPackagesToDeliver(100, list);
//		
//		List<String> expectedRes = new ArrayList<String>();
//		expectedRes.add("PKG1 87.5 787.5 0.07");
//		expectedRes.add("PKG2 0.0 1625.0 0.07");
//		expectedRes.add("PKG3 55.0 1045.0 1.42");
//		
//		List<String> actualRes = DeliveryUtil.getInstance().deliveryPackageListToExpectedFormat(deliveryPackageList);
//		
//		assertEquals(list.size(), actualRes.size());
//		assertEquals(expectedRes, actualRes);
		
		List<String> list2 = new ArrayList<String>();
		list2.add("PKG1 50 30 OFR001");
		list2.add("PKG2 75 125 OFR0008");
		list2.add("PKG3 175 100 OFR003");
		list2.add("PKG4 110 60 OFR002");
		list2.add("PKG5 155 95 NA");
		
		List<DeliveryPackage> deliveryPackageList2 = DeliveryService.getInstance().
				sendPackagesToDeliver(100, list2);
		
		List<String> expectedRes2 = new ArrayList<String>();
		expectedRes2.add("PKG1 0.0 750.0 3.98");
		expectedRes2.add("PKG2 0.0 1475.0 1.78");
		expectedRes2.add("PKG3 0.0 2350.0 1.42");
		expectedRes2.add("PKG4 105.0 1395.0 0.85");
		expectedRes2.add("PKG5 0.0 2125.0 4.19");
		
		List<String> actualRes2 = DeliveryUtil.deliveryPackageListToExpectedFormat(deliveryPackageList2);
		
		assertEquals(list2.size(), actualRes2.size());
		assertEquals(expectedRes2, actualRes2);
	}
}
