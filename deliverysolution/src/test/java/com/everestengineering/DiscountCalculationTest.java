package com.everestengineering;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.everestengineering.model.DiscountResponse;
import com.everestengineering.service.DiscountService;

public class DiscountCalculationTest {
	
	Logger logger = Logger.getLogger(DiscountCalculationTest.class);

	@Test
	public void testDiscountCalculation() {
		
		DiscountResponse discountResponse = DiscountService.getInstance().
				calculateDiscountByCouponCode("OFR001", 100, 5, 5);
		assertEquals(0.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().
				calculateDiscountByCouponCode("OFR001", 100, 75, 5);
		assertEquals(10.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode("OFR002", 1500,
				15, 5);
		assertEquals(0.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode("OFR002", 1500,
				135, 75);
		assertEquals(105.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode("OFR003", 700,
				10, 100);
		assertEquals(35.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode("OFR003", 700,
				5, 100);
		assertEquals(0.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode("OFR004", 700,
				10, 100);
		assertEquals(0.0, discountResponse.getTotalDiscountInAmount());
	}
}
