package com.everestengineering.discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.everestengineering.discount.constant.CostConstants;
import com.everestengineering.discount.model.OrderResponse;
import com.everestengineering.discount.util.ValidationUtil;

public class ValidationTest {

	Logger logger = Logger.getLogger(ValidationTest.class);

	@Test
	public void testForValidWeightAndDistance() {

		OrderResponse orderResponse = ValidationUtil.validateParseDouble("15", CostConstants.WEIGHT_IN_KG,
				new OrderResponse());
		assertEquals(true, orderResponse.isValid());

		orderResponse = ValidationUtil.validateParseDouble("15r34wr3", CostConstants.WEIGHT_IN_KG,
				new OrderResponse());
		assertEquals(false, orderResponse.isValid());
	}

	@Test
	public void testToValidateBaseDeliveryCostAndNoOfPackages() {

		OrderResponse orderResponse = ValidationUtil.validateBaseDeliveryCostAndNoOfPackages("100 3",
				new OrderResponse());
		assertEquals(true, orderResponse.isValid());

		orderResponse = ValidationUtil.validateBaseDeliveryCostAndNoOfPackages("100rwe 3",
				new OrderResponse());
		assertEquals(false, orderResponse.isValid());

		orderResponse = ValidationUtil.validateBaseDeliveryCostAndNoOfPackages("100 3rfwe",
				new OrderResponse());
		assertEquals(false, orderResponse.isValid());
	}
	
	@Test
	public void testToValidatePackageIdWeightInKgDistInKmAndOffCode() {

		OrderResponse orderResponse = ValidationUtil.
				validatePackageIdWeightInKgDistInKmAndOffCode("PKG1 5 5 OFR001", new OrderResponse());
		assertEquals(true, orderResponse.isValid());

		orderResponse = ValidationUtil.validatePackageIdWeightInKgDistInKmAndOffCode("PKG1 5 5 ",
				new OrderResponse());
		assertEquals(false, orderResponse.isValid());

		orderResponse = ValidationUtil.validatePackageIdWeightInKgDistInKmAndOffCode("",
				new OrderResponse());
		assertEquals(false, orderResponse.isValid());
	}
}
