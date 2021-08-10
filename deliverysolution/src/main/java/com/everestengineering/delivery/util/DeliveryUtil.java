package com.everestengineering.delivery.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
import com.everestengineering.discount.constant.ValidationConstant;
import com.everestengineering.discount.model.DeliveryVehicle;
import com.everestengineering.discount.model.DiscountResponse;
import com.everestengineering.discount.service.CostService;

public class DeliveryUtil {

	static Logger logger = Logger.getLogger(DeliveryUtil.class);

	private DeliveryUtil() {

	}

	public static void findAllCombos(List<DeliveryPackage> pckgs, int target,
			ArrayList<DeliveryPackage> partial, List<String> allCombos,
			Map<String, DeliveryPackage> mapOfPkgIdWithDeliveryPackage) {

		String key = "";
		double weights = 0;
		double distance = 0;
		for (DeliveryPackage dpckg : partial) {
			weights = weights + dpckg.getWeightInKg();
			distance = distance + dpckg.getDistanceInKms();
			key = key.length() == 0 ? key + dpckg.getPackageId() : key + "_" + dpckg.getPackageId();

			if (mapOfPkgIdWithDeliveryPackage.get(dpckg.getPackageId()) == null) {
				mapOfPkgIdWithDeliveryPackage.put(dpckg.getPackageId(), dpckg);
			}
		}

		if (key.length() > 0 && weights <= target) {
			if (pckgs.size() > 0 && (key.contains("_") || weights == target)) {
				allCombos.add(weights + "_" + distance + "_" + key);
			} else {
				allCombos.add(weights + "_" + distance + "_" + key);
			}
		}

		for (int i = 0; i < pckgs.size(); i++) {
			ArrayList<DeliveryPackage> remaining = new ArrayList<DeliveryPackage>();
			DeliveryPackage pckg = pckgs.get(i);
			for (int j = i + 1; j < pckgs.size(); j++)
				remaining.add(pckgs.get(j));
			ArrayList<DeliveryPackage> partialRec = new ArrayList<DeliveryPackage>(partial);
			partialRec.add(pckg);
			findAllCombos(remaining, target, partialRec, allCombos, mapOfPkgIdWithDeliveryPackage);
		}
	}

	public static String sortAndGetBestPackageCombo(List<String> allCombos,
			Map<String, DeliveryPackage> mapOfPkgIdWithDeliveryPackage) {
		// sort packages based on weight and distance
		Collections.sort(allCombos, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return extractInt(o1).intValue() - extractInt(o2).intValue();
			}

			Long extractInt(String s) {
				String num = s.replaceAll("\\D", "");
				// return 0 if no digits found
				return num.isEmpty() ? 0 : Long.parseLong(num);
			}
		}.reversed());

		List<String> finalCombo = new ArrayList<String>(allCombos);

		List<String> finalListWithMaxPckMinDist = finalCombo.stream()
				.filter(o -> o.split("_")[0].equals(finalCombo.get(0).split("_")[0]))
				.filter(o -> o.split("_").length == finalCombo.get(0).split("_").length)
				.collect(Collectors.toList());
		
		String bestCombination = finalListWithMaxPckMinDist.get(finalListWithMaxPckMinDist.size() - 1);
		
		logger.debug("Best combination found to deliver is " + bestCombination);
		
		return bestCombination;
		
//		String[] bestCombArr = bestCombination.split("_");
//		
//		int count = 0;
//		List<DeliveryPackage> finalListOfPckRdyForDlvr = new ArrayList<DeliveryPackage>();
//		for(String bestPkgId: bestCombArr) {
//			if(count == 0 || count == 1) {
//				count++;
//				continue;
//			}
//			
//			DeliveryPackage dlvryPckg = mapOfPkgIdWithDeliveryPackage.get(bestPkgId);
//			
//			if(null != dlvryPckg) {
//				dlvryPckg = setCostAndDiscountToPckgDelivery(dlvryPckg);
//				finalListOfPckRdyForDlvr.add(mapOfPkgIdWithDeliveryPackage.get(bestPkgId));
//			}
//		}
//		
//		return finalListOfPckRdyForDlvr;
	}
	
	public static List<DeliveryPackage>  getDeliveryPckgFromBestCombination(String bestCombination,
			Map<String, DeliveryPackage> mapOfPkgIdWithDeliveryPackage){
		String[] bestCombArr = bestCombination.split("_");
		
		int count = 0;
		List<DeliveryPackage> finalListOfPckRdyForDlvr = new ArrayList<DeliveryPackage>();
		for(String bestPkgId: bestCombArr) {
			if(count == 0 || count == 1) {
				count++;
				continue;
			}
			
			DeliveryPackage dlvryPckg = mapOfPkgIdWithDeliveryPackage.get(bestPkgId);
			
			if(null != dlvryPckg) {
				dlvryPckg = setCostAndDiscountToPckgDelivery(dlvryPckg);
				finalListOfPckRdyForDlvr.add(mapOfPkgIdWithDeliveryPackage.get(bestPkgId));
			}
		}
		
		return finalListOfPckRdyForDlvr;
	}
	
	public static DeliveryPackage setCostAndDiscountToPckgDelivery(DeliveryPackage pckg) {
		
		DiscountResponse discountResponse = CostService.getInstance().calculateFinalDeliveryCost(
				pckg.getBaseCostOfDelivery(), pckg.getWeightInKg(),
				pckg.getDistanceInKms(),
				pckg.getDiscountCoupon() != null
						? pckg.getDiscountCoupon().getCouponCode()
						: ValidationConstant.INVALID_INPUT);
		pckg.setDiscountResponse(discountResponse);
		
		return pckg;
	}
	
	public static Map<String, DeliveryVehicle> getNextAvailableVehicleInTransit(
			Map<String, DeliveryVehicle> vehiclesInTransit) {
		
		Map<String, Double> mapOfVhcleAndNxtAvalbleTime = vehiclesInTransit.entrySet()
		        .stream()
		        .collect(Collectors.toMap(Map.Entry::getKey,
		                                  e -> new Double(e.getValue().getNextAvailableInHrs())));
		
		Map<String, Double> sortedByCount = mapOfVhcleAndNxtAvalbleTime.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Double>comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
		Map<String, DeliveryVehicle> newMap = vehiclesInTransit.entrySet()
		.stream()
		.filter(x -> x.getKey().equals(sortedByCount.entrySet().iterator().next().getKey()))
		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
		return newMap;
	}
	
	public static Map<String, DeliveryVehicle> assignPackagetoVehicle(
			List<DeliveryPackage> listOfPckgRdyForDlvry, List<DeliveryPackage> masterPackageList) {

		// get available vehicle fleets
		Map<String, DeliveryVehicle> availableVehicleFleets = VehicleUtil.getAvailableVehicleFleets();
		DeliveryVehicle vehicleAvlbleForDelivery = null;
		
		if(availableVehicleFleets.size() == 0) {
			logger.debug("There are no available vehicles, need to check the next available from fleet");
			availableVehicleFleets = getNextAvailableVehicleInTransit(VehicleUtil.getVehiclesInTransit());
		}
		
		vehicleAvlbleForDelivery = availableVehicleFleets.entrySet().iterator().next().getValue();
		logger.debug("Next available vehicle in the fleet is " +
				availableVehicleFleets.entrySet().iterator().next().getValue().getVId() + " after " +
				vehicleAvlbleForDelivery.getNextAvailableInHrs() + " hrs");
		
		// get packages ready for delivery
		
		double totalTimeOfDeliveryOfPckg = 0;
		
		// adding all the details required for package to be delivered ie cost, discount, time required ...
		for (DeliveryPackage packageReadyForDelivery : listOfPckgRdyForDlvry) {

			if (null != packageReadyForDelivery) {
				
				double totTimeToDeliverPckge = DeliveryUtil.calculateDeliveryTime(packageReadyForDelivery.getDistanceInKms());
				
				packageReadyForDelivery.
				setTimeTakenToDeliverInHrs(
						DeliveryUtil.getDoubleValueRoundedToTwo(vehicleAvlbleForDelivery.getNextAvailableInHrs() +
								totTimeToDeliverPckge));
				
				totalTimeOfDeliveryOfPckg = totalTimeOfDeliveryOfPckg == 0
						? (totalTimeOfDeliveryOfPckg + totTimeToDeliverPckge)
						: Math.max(totalTimeOfDeliveryOfPckg, totTimeToDeliverPckge);
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
			
			if(null != vehicleReadyToFlagOff.getDeliveryPackages()) {
				vehicleReadyToFlagOff.getDeliveryPackages().addAll(listOfPckgRdyForDlvry);
			} else {
				vehicleReadyToFlagOff.setDeliveryPackages(listOfPckgRdyForDlvry);
			}
			vehicleReadyToFlagOff.setNextAvailableInHrs(totalTimeOfDeliveryOfPckg);
			VehicleUtil.setVehiclesInTransit(vehicleReadyToFlagOff);
			availableVehicleFleets.remove(vehicleReadyToFlagOff.getVId());
		}
		return VehicleUtil.getVehiclesInTransit();
	}

	public static double calculateDeliveryTime(double distance) {

		System.out.println("VehicleUtil.maxSpeed " + VehicleUtil.maxSpeed);
		
		Double timeInHrs = distance / VehicleUtil.maxSpeed;
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
		List<String> dlPckgIds = deliveryPckgs.stream().map(DeliveryPackage::getPackageId).collect(Collectors.toList());
		return dlPckgIds;
	}

	public static List<String> deliveryPackageListToExpectedFormat(List<DeliveryPackage> deliveryPackageList) {

		List<String> deliveredPckgs = deliveryPackageList.stream()
				.map(pckg -> new DeliveryPackage(pckg.getPackageId(),
						pckg.getDiscountResponse().getTotalDiscountInAmount(),
						pckg.getDiscountResponse().getFinalDeliveryCost(), pckg.getTimeTakenToDeliverInHrs())
								.getDeliveredPckgDetails())
				.collect(Collectors.toList());
		Collections.sort(deliveredPckgs);
		return deliveredPckgs;
	}
	
	public static List<String> getPckIdsFromList(List<DeliveryPackage> deliveryPckgs) {
		List<String> dlPckgIds = deliveryPckgs.stream().map(DeliveryPackage::getPackageId).collect(Collectors.toList());
		return dlPckgIds;
	}
}
