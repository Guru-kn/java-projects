package com.everestengineering.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.service.DeliveryService;
import com.everestengineering.delivery.util.DeliveryUtil;
import com.everestengineering.delivery.util.VehicleUtil;

public class DeliveryServiceTest {
	
	Logger logger = Logger.getLogger(DeliveryServiceTest.class);
	
	@Test
	public void testAllCombos() {
		

		
		List<DeliveryPackage> pckgs = new ArrayList<DeliveryPackage>();
		List<String> listOfPackageToDeliver = new ArrayList<String>();
		
		int target = VehicleUtil.maxCarriableWeight.intValue();
		
		listOfPackageToDeliver.add("PKG1 50 65 OFR002");
		listOfPackageToDeliver.add("PKG2 75 100 OFR003");
		listOfPackageToDeliver.add("PKG3 150 50 OFR001");
		listOfPackageToDeliver.add("PKG4 200 50 OFR001");
		
		pckgs = DeliveryService.getInstance().
				readPackageDetails(listOfPackageToDeliver, 100d);
		
		List<String> allCombos = new ArrayList<String>();
		
		Map<String, DeliveryPackage> mapOfPkgIdWithDeliveryPackage = new TreeMap<String, DeliveryPackage>();
		
		DeliveryUtil.findAllCombos(pckgs, target, new ArrayList<DeliveryPackage>(), allCombos, mapOfPkgIdWithDeliveryPackage);
		String bestCombination = DeliveryUtil.sortAndGetBestPackageCombo(allCombos, mapOfPkgIdWithDeliveryPackage);
		
		assertEquals("200.0_115.0_PKG1_PKG3", bestCombination);
		
		pckgs = new ArrayList<DeliveryPackage>();
		listOfPackageToDeliver = new ArrayList<String>();
		
		listOfPackageToDeliver.add("PKG5 25 65 OFR002");
		listOfPackageToDeliver.add("PKG3 50 100 OFR003");
		listOfPackageToDeliver.add("PKG1 75 50 OFR001");
		listOfPackageToDeliver.add("PKG2 75 15 OFR002");
		listOfPackageToDeliver.add("PKG6 75 10 OFR002");
		listOfPackageToDeliver.add("PKG4 100 50 OFR002");
		
		pckgs = DeliveryService.getInstance().
				readPackageDetails(listOfPackageToDeliver, 100d);
		
		allCombos = new ArrayList<String>();
		
		mapOfPkgIdWithDeliveryPackage = new TreeMap<String, DeliveryPackage>();
		
		DeliveryUtil.findAllCombos(pckgs, target, new ArrayList<DeliveryPackage>(), allCombos, mapOfPkgIdWithDeliveryPackage);
		bestCombination = DeliveryUtil.sortAndGetBestPackageCombo(allCombos, mapOfPkgIdWithDeliveryPackage);
		
		assertEquals("200.0_125.0_PKG3_PKG2_PKG6", bestCombination);
		
		pckgs = new ArrayList<DeliveryPackage>();
		listOfPackageToDeliver = new ArrayList<String>();
		
		listOfPackageToDeliver.add("PKG2 50 65 OFR002");
		listOfPackageToDeliver.add("PKG3 150 100 OFR003");
		listOfPackageToDeliver.add("PKG1 200 50 OFR001");
		
		pckgs = DeliveryService.getInstance().
				readPackageDetails(listOfPackageToDeliver, 100d);
		
		allCombos = new ArrayList<String>();
		
		mapOfPkgIdWithDeliveryPackage = new TreeMap<String, DeliveryPackage>();
		
		DeliveryUtil.findAllCombos(pckgs, target, new ArrayList<DeliveryPackage>(), allCombos, mapOfPkgIdWithDeliveryPackage);
		bestCombination = DeliveryUtil.sortAndGetBestPackageCombo(allCombos, mapOfPkgIdWithDeliveryPackage);
		
		assertEquals("200.0_165.0_PKG2_PKG3", bestCombination);
		
		pckgs = new ArrayList<DeliveryPackage>();
		listOfPackageToDeliver = new ArrayList<String>();
		
		listOfPackageToDeliver.add("PKG2 50 65 OFR002");
		listOfPackageToDeliver.add("PKG3 75 100 OFR003");
		listOfPackageToDeliver.add("PKG1 175 50 OFR001");
		listOfPackageToDeliver.add("PKG4 110 50 OFR001");
		listOfPackageToDeliver.add("PKG5 155 50 OFR001");
		
		pckgs = DeliveryService.getInstance().
				readPackageDetails(listOfPackageToDeliver, 100d);
		
		allCombos = new ArrayList<String>();
		
		mapOfPkgIdWithDeliveryPackage = new TreeMap<String, DeliveryPackage>();
		
		DeliveryUtil.findAllCombos(pckgs, target, new ArrayList<DeliveryPackage>(), allCombos, mapOfPkgIdWithDeliveryPackage);
		bestCombination = DeliveryUtil.sortAndGetBestPackageCombo(allCombos, mapOfPkgIdWithDeliveryPackage);
		
		assertEquals("185.0_150.0_PKG3_PKG4", bestCombination);
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
		
		VehicleUtil.addDeliveryVehicles("2 70 200");
		
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
