package com.everestengineering;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.everestengineering.constant.CostConstants;
import com.everestengineering.model.OrderResponse;
import com.everestengineering.util.ValidationUtil;

public class ValidationTest {

	Logger logger = Logger.getLogger(ValidationTest.class);

	@Test
	public void testForValidWeightAndDistance() {

		OrderResponse orderResponse = ValidationUtil.getInstance().validateParseDouble("15", CostConstants.WEIGHT_IN_KG,
				new OrderResponse());
		assertEquals(true, orderResponse.isValid());

		orderResponse = ValidationUtil.getInstance().validateParseDouble("15r34wr3", CostConstants.WEIGHT_IN_KG,
				new OrderResponse());
		assertEquals(false, orderResponse.isValid());
	}

	@Test
	public void testToValidateBaseDeliveryCostAndNoOfPackages() {

		OrderResponse orderResponse = ValidationUtil.getInstance().validateBaseDeliveryCostAndNoOfPackages("100 3",
				new OrderResponse());
		assertEquals(true, orderResponse.isValid());

		orderResponse = ValidationUtil.getInstance().validateBaseDeliveryCostAndNoOfPackages("100rwe 3",
				new OrderResponse());
		assertEquals(false, orderResponse.isValid());

		orderResponse = ValidationUtil.getInstance().validateBaseDeliveryCostAndNoOfPackages("100 3rfwe",
				new OrderResponse());
		assertEquals(false, orderResponse.isValid());
	}
	
	@Test
	public void testToValidatePackageIdWeightInKgDistInKmAndOffCode() {

		OrderResponse orderResponse = ValidationUtil.getInstance().
				validatePackageIdWeightInKgDistInKmAndOffCode("PKG1 5 5 OFR001", new OrderResponse());
		assertEquals(true, orderResponse.isValid());

		orderResponse = ValidationUtil.getInstance().validatePackageIdWeightInKgDistInKmAndOffCode("PKG1 5 5 ",
				new OrderResponse());
		assertEquals(false, orderResponse.isValid());

		orderResponse = ValidationUtil.getInstance().validatePackageIdWeightInKgDistInKmAndOffCode("",
				new OrderResponse());
		assertEquals(false, orderResponse.isValid());
	}
}
