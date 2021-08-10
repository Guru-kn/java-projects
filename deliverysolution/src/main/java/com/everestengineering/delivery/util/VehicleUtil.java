package com.everestengineering.delivery.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.everestengineering.delivery.constant.VehicleConstant;
import com.everestengineering.discount.model.DeliveryVehicle;

public class VehicleUtil {
	
	private static Map<String, DeliveryVehicle> vehicleFleets;
	private static Map<String, DeliveryVehicle> availableVehicleFleets;
	private static Map<String, DeliveryVehicle> vehiclesInTransit;
	
	public static int noOfVehicles = 2;
	public static Double maxSpeed = 70d;
	public static Double maxCarriableWeight = 200d;
	
	private VehicleUtil() {
		
	}

	public static void addDeliveryVehicles(String vehicleDetails) {
		
		String[] vehicleDetailsArr = null;
		if(null != vehicleDetails) {
			vehicleDetailsArr = vehicleDetails.split(" ");
			noOfVehicles = Integer.parseInt(vehicleDetailsArr[0]);
			maxSpeed = Double.parseDouble(vehicleDetailsArr[1]);
			maxCarriableWeight = Double.parseDouble(vehicleDetailsArr[2]);
		}
		
		if(vehicleFleets == null) {
			vehicleFleets = new HashMap<String, DeliveryVehicle>();
		}
		
		if(availableVehicleFleets == null) {
			availableVehicleFleets = new HashMap<String, DeliveryVehicle>();
		}
		
		for(int i=0; i < noOfVehicles; i++) {
			DeliveryVehicle deliveryVehicle = new DeliveryVehicle(
					VehicleConstant.VehicleIds.VEHICLE01.toString(),true,0d);
			String vehicleId = VehicleConstant.VehicleIds.VEHICLE + (i < 10 ? "0" : "") + (i+1); 
			vehicleFleets.put(vehicleId, deliveryVehicle);
		}
		
//		DeliveryVehicle deliveryVehicle = new DeliveryVehicle(
//				VehicleConstant.VehicleIds.VEHICLE01.toString(),true,0d);
//		vehicleFleets.put(VehicleConstant.VehicleIds.VEHICLE01.toString(), deliveryVehicle);
//		
//		deliveryVehicle = new DeliveryVehicle(
//				VehicleConstant.VehicleIds.VEHICLE02.toString(),true,0d);
//		vehicleFleets.put(VehicleConstant.VehicleIds.VEHICLE02.toString(), deliveryVehicle);
		
		availableVehicleFleets = vehicleFleets.entrySet()
                .stream()
                .collect(
                    Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
	}
	
	public static Map<String, DeliveryVehicle> getVehicleFleets() {
		return vehicleFleets;
	}
	
	public static Map<String, DeliveryVehicle> getAvailableVehicleFleets() {
		return availableVehicleFleets;
	}
	
	public static void setVehiclesInTransit(DeliveryVehicle deliveryVehicle) {
		
		if(null == vehiclesInTransit) {
			vehiclesInTransit = new HashMap<String, DeliveryVehicle>();
		}
		
		vehiclesInTransit.put(deliveryVehicle.getVId(), deliveryVehicle);
	}
	
	public static Map<String, DeliveryVehicle> getVehiclesInTransit() {
		return vehiclesInTransit;
	}
}
