package com.everestengineering.discount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.everestengineering.discount.constant.DiscountConstant;
import com.everestengineering.discount.constant.ValidationConstant;
import com.everestengineering.discount.model.DiscountResponse;
import com.everestengineering.discount.service.DiscountService;

public class DiscountCalculationTest {
	
	Logger logger = Logger.getLogger(DiscountCalculationTest.class);

	@Test
	public void testDiscountCalculation() {
		
		DiscountResponse discountResponse = DiscountService.getInstance().
				calculateDiscountByCouponCode(DiscountConstant.CouponNames.OFR001.toString(), 100, 5, 5);
		assertEquals(0.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().
				calculateDiscountByCouponCode(DiscountConstant.CouponNames.OFR001.toString(), 100, 75, 5);
		assertEquals(10.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode(
				DiscountConstant.CouponNames.OFR002.toString(), 1500, 15, 5);
		assertEquals(0.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode(
				DiscountConstant.CouponNames.OFR002.toString(), 1500, 135, 75);
		assertEquals(105.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode(
				DiscountConstant.CouponNames.OFR003.toString(), 700,
				10, 100);
		assertEquals(35.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode(
				DiscountConstant.CouponNames.OFR003.toString(), 700,
				5, 100);
		assertEquals(0.0, discountResponse.getTotalDiscountInAmount());
		
		discountResponse = DiscountService.getInstance().calculateDiscountByCouponCode(
				ValidationConstant.INVALID_INPUT, 700, 10, 100);
		assertEquals(0.0, discountResponse.getTotalDiscountInAmount());
	}
}
