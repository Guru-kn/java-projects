package com.everestengineering.delivery;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.everestengineering.delivery.model.DeliveryPackage;
import com.everestengineering.delivery.service.DeliveryService;

public class DeliveryServiceTest {
	
	Logger logger = Logger.getLogger(DeliveryServiceTest.class);

	@Test
	public void testDeliveryService() {
		
		List<DeliveryPackage> deliveryPackageList = DeliveryService.getInstance().
				sendPackagesToDeliver(100, new ArrayList<String>());
		
	}
}
