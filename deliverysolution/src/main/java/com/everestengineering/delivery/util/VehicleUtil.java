package com.everestengineering.delivery.util;

import java.util.Map;

import com.everestengineering.delivery.constant.VehicleConstant;
import com.everestengineering.discount.model.DeliveryVehicle;

public class VehicleUtil {
	
	private static Map<String, DeliveryVehicle> vehicleFleets;
	
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
		
		DeliveryVehicle deliveryVehicle = new DeliveryVehicle(
				VehicleConstant.VehicleIds.VEHICLE01.toString(),true,0);
		vehicleFleets.put(VehicleConstant.VehicleIds.VEHICLE01.toString(), deliveryVehicle);
		
		deliveryVehicle = new DeliveryVehicle(
				VehicleConstant.VehicleIds.VEHICLE02.toString(),true,0);
		vehicleFleets.put(VehicleConstant.VehicleIds.VEHICLE02.toString(), deliveryVehicle);
	}
	
	public static Map<String, DeliveryVehicle> getVehicleFleets() {
		if(null == vehicleFleets) {
			addDeliveryVehicles(); // this needs to either in cache or driven from DB
		}
		return vehicleFleets;
	}
}
