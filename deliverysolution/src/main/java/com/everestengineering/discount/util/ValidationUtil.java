package com.everestengineering.discount.util;

import com.everestengineering.delivery.constant.VehicleConstant;
import com.everestengineering.discount.constant.CostConstants;
import com.everestengineering.discount.constant.DiscountConstant;
import com.everestengineering.discount.constant.ValidationConstant;
import com.everestengineering.discount.model.OrderResponse;

public class ValidationUtil {

	private ValidationUtil() {
		
	}

	public static OrderResponse validateBaseDeliveryCostAndNoOfPackages(String baseDlvryCostNoOfPackages,
			OrderResponse orderResponse) {

		String[] baseDlvryCostNoOfPackagesArr = null;
		try {
			baseDlvryCostNoOfPackagesArr = baseDlvryCostNoOfPackages.split(" ");
			if (baseDlvryCostNoOfPackagesArr.length == 0) {
				orderResponse.setValid(false);
				orderResponse.setValidationMessage(
						"Enter the base delivery cost and number of packages in this format `100 3`");
			} else {

				orderResponse = validateParseDouble(baseDlvryCostNoOfPackagesArr[0], CostConstants.BASE_DELIVERY_COST,
						orderResponse);

				if (orderResponse.isValid()) {
					orderResponse = validateParseDouble(baseDlvryCostNoOfPackagesArr[1],
							CostConstants.NUMBER_OF_PACKAGES, orderResponse);
				}
			}
		} catch (Exception e) {
			orderResponse.setValid(false);
			orderResponse.setValidationMessage(ValidationConstant.INVALID_INPUT);
		}

		return orderResponse;
	}
	
	public static OrderResponse validateVehicleDetails(String vehicleDetails,
			OrderResponse orderResponse) {

		String[] vehicleDetailsArr = null;
		try {
			vehicleDetailsArr = vehicleDetails.split(" ");
			if (vehicleDetailsArr.length == 0) {
				orderResponse.setValid(false);
				orderResponse.setValidationMessage(
						"Enter the vehicle details in this format `no_of_vehicles max_speed max_carriable_weight`");
			} else {

				orderResponse = validateParseInt(vehicleDetailsArr[0], VehicleConstant.NUMBER_OF_VEHICLES,
						orderResponse);

				if (orderResponse.isValid()) {
					orderResponse = validateParseDouble(vehicleDetailsArr[1],
							VehicleConstant.MAX_SPEED_OF_VEHICLE, orderResponse);
				} else {
					return orderResponse;
				}
				
				if (orderResponse.isValid()) {
					orderResponse = validateParseDouble(vehicleDetailsArr[1],
							VehicleConstant.MAX_WEIGHT_VEHICLE_CARRY, orderResponse);
				} else {
					return orderResponse;
				}
			}
		} catch (Exception e) {
			orderResponse.setValid(false);
			orderResponse.setValidationMessage(ValidationConstant.INVALID_INPUT);
		}

		return orderResponse;
	}

	public static OrderResponse validatePackageIdWeightInKgDistInKmAndOffCode(String pkgIdPkgWeightInKgDistInKmOffCode,
			OrderResponse orderResponse) {

		try {
			orderResponse = new OrderResponse();
			String[] pkgIdPkgWeightInKgDistInKmOffCodeArr = pkgIdPkgWeightInKgDistInKmOffCode.split(" ");
			if (pkgIdPkgWeightInKgDistInKmOffCodeArr.length < 4) {
				orderResponse.setValid(false);
				orderResponse.setValidationMessage(
						"Entered package details should be in this format `pkg_id pkg_weight_in_kg distance_in_km offer_code`");
			} else {

				orderResponse = validateString(pkgIdPkgWeightInKgDistInKmOffCodeArr[0], CostConstants.PACKAGE_ID,
						orderResponse);

				if (orderResponse.isValid()) {
					orderResponse = validateParseDouble(pkgIdPkgWeightInKgDistInKmOffCodeArr[1],
							CostConstants.WEIGHT_IN_KG, orderResponse);
				} else {
					return orderResponse;
				}

				if (orderResponse.isValid()) {
					orderResponse = validateParseDouble(pkgIdPkgWeightInKgDistInKmOffCodeArr[2],
							CostConstants.DISTANCE_IN_KM, orderResponse);
				} else {
					return orderResponse;
				}

				if (orderResponse.isValid()) {
					orderResponse = validateString(pkgIdPkgWeightInKgDistInKmOffCodeArr[3], DiscountConstant.COUPON_CODE,
							orderResponse);
				} else {
					return orderResponse;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			orderResponse.setValid(false);
			orderResponse.setValidationMessage(ValidationConstant.INVALID_INPUT);
		}

		return orderResponse;
	}

	public static OrderResponse validateParseDouble(String strToParse, String parameterName, OrderResponse orderResponse) {
		try {
			double val = Double.valueOf(strToParse);
			
			if(val > 0) {
				orderResponse.setValid(true);
				orderResponse.setValidationMessage(parameterName + " Validated");
			} else {
				orderResponse.setValid(false);
				orderResponse.setValidationMessage("Entered " + parameterName + " should be a greater than 0");
			}
		} catch (NumberFormatException e) {
			orderResponse.setValid(false);
			orderResponse.setValidationMessage("Entered " + parameterName + " should be a number");
		} catch (Exception e) {
			orderResponse.setValid(false);
			orderResponse.setValidationMessage(ValidationConstant.INVALID_INPUT);
		}

		return orderResponse;
	}
	
	public static OrderResponse validateParseInt(String strToParse, String parameterName, OrderResponse orderResponse) {
		try {
			int val = Integer.valueOf(strToParse);
			
			if(val > 0) {
				orderResponse.setValid(true);
				orderResponse.setValidationMessage(parameterName + " Validated");
			} else {
				orderResponse.setValid(false);
				orderResponse.setValidationMessage("Entered " + parameterName + " should be a greater than 0");
			}
		} catch (NumberFormatException e) {
			orderResponse.setValid(false);
			orderResponse.setValidationMessage("Entered " + parameterName + " should be a number");
		} catch (Exception e) {
			orderResponse.setValid(false);
			orderResponse.setValidationMessage(ValidationConstant.INVALID_INPUT);
		}

		return orderResponse;
	}

	public static OrderResponse validateString(String strToValidate, String parameterName, OrderResponse orderResponse) {
		if (null == strToValidate || strToValidate.trim().length() == 0) {
			orderResponse.setValid(false);
			orderResponse.setValidationMessage("Entered " + parameterName + " is null or empty");
		}
		orderResponse.setValid(true);
		orderResponse.setValidationMessage(parameterName + " Validated");

		return orderResponse;
	}
}
