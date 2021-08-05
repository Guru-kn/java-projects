package com.everestengineering.discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.everestengineering.discount.util.CostUtil;

public class OrderCalculationTest {
	
	Logger logger = Logger.getLogger(OrderCalculationTest.class);

	@Test
	public void testBaseCostCalculation() {
		assertEquals(175.0, CostUtil.getInstance().calculateTotalCost(100, 5, 5));
		assertEquals(1045.0, CostUtil.getInstance().calculateTotalCost(500, 15.5, 78));
		assertEquals(2256.575, CostUtil.getInstance().calculateTotalCost(500, 150.23, 50.855));
		assertEquals(8250, CostUtil.getInstance().calculateTotalCost(500, 500, 550));
	}
}
