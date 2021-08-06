package com.everestengineering.delivery;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.everestengineering.delivery.service.DeliveryService;

public class DeliveryMainApp {
	
	static Logger logger = Logger.getLogger(DeliveryMainApp.class);
	
	public static void main(String[] args) {
		
		Scanner input = null;
		try {
			logger.info("Enter the base delivery cost and number of packages in 100 5 format ");
			input = new Scanner(System.in);
			String baseCostOfDeliveryAndNoOfPckgs = input.nextLine();
			DeliveryService.getInstance().sendPackagesToDeliver(baseCostOfDeliveryAndNoOfPckgs);
		}catch (Exception e) {
			logger.error(e);
		}
	}
}
