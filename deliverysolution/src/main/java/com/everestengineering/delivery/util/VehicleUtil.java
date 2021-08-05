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
	
	private VehicleUtil() {
		
	}
	
	public static VehicleUtil vehicleUtil = null;

	static {
		if (vehicleUtil == null)
			vehicleUtil = new VehicleUtil();
	}

	public static VehicleUtil getInstance() {
		if(null == vehicleFleets) {
			addDeliveryVehicles(); // this needs to either in cache or driven from DB
		}
		return vehicleUtil;
	}

	private static void addDeliveryVehicles() {
		// TODO Auto-generated method stub
		
		if(vehicleFleets == null) {
			vehicleFleets = new HashMap<String, DeliveryVehicle>();
		}
		
		if(availableVehicleFleets == null) {
			availableVehicleFleets = new HashMap<String, DeliveryVehicle>();
		}
		
		DeliveryVehicle deliveryVehicle = new DeliveryVehicle(
				VehicleConstant.VehicleIds.VEHICLE01.toString(),true,0d);
		vehicleFleets.put(VehicleConstant.VehicleIds.VEHICLE01.toString(), deliveryVehicle);
		
		deliveryVehicle = new DeliveryVehicle(
				VehicleConstant.VehicleIds.VEHICLE02.toString(),true,0d);
		vehicleFleets.put(VehicleConstant.VehicleIds.VEHICLE02.toString(), deliveryVehicle);
		
		availableVehicleFleets = vehicleFleets.entrySet()
                .stream()
                .collect(
                    Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
	}
	
	public static Map<String, DeliveryVehicle> getVehicleFleets() {
		if(null == vehicleFleets) {
			addDeliveryVehicles(); // this needs to either in cache or driven from DB
		}
		return vehicleFleets;
	}
	
	public static Map<String, DeliveryVehicle> getAvailableVehicleFleets() {
		if(null == vehicleFleets) {
			addDeliveryVehicles(); // this needs to either in cache or driven from DB
		}
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
