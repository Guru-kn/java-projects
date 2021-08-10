package com.everestengineering.delivery.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.everestengineering.discount.constant.DiscountConstant.DiscountMeasure;
import com.everestengineering.discount.constant.DiscountConstant.DiscountType;
import com.everestengineering.discount.constant.DiscountConstant.RangeMeasure;
import com.everestengineering.discount.model.DiscountCoupon;
import com.everestengineering.discount.model.DiscountCriteria;

public class ExcelUtil {

	public static Map<String, DiscountCoupon> readFromExcelFileAndLoadDiscounts() {
		
		Map<String, DiscountCoupon> discountCoupons = null;
		
		XSSFWorkbook workbook = null;

		// Get first/desired sheet from the workbook
		XSSFSheet sheet = null;
		
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream file = classloader.getResourceAsStream("discountcoupons.xlsx");
			
			discountCoupons = new HashMap<String, DiscountCoupon>();

			// Create Workbook instance holding reference to .xlsx file
			workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				
				DiscountCriteria discountCriteria = null;
				Cell cell = row.getCell(0);
				if (cell != null && row.getRowNum() > 0) {
					// For each row, iterate through all the columns
					// constructor values for discountCriteria
					// discountType, discountMeasure, rangeMeasure, discountValue,fromWeight,
					// toWeight, fromDistance, toDistance
					discountCriteria = new 
							DiscountCriteria(DiscountType.RANGE,
							DiscountMeasure.valueOf(row.getCell(5).getStringCellValue()),
							RangeMeasure.BOTH, 
							DeliveryUtil.getDoubleValueFromString(new DataFormatter().formatCellValue(row.getCell(6))),
							DeliveryUtil.getDoubleValueFromString(new DataFormatter().formatCellValue(row.getCell(3))), 
							DeliveryUtil.getDoubleValueFromString(new DataFormatter().formatCellValue(row.getCell(4))), 
							DeliveryUtil.getDoubleValueFromString(new DataFormatter().formatCellValue(row.getCell(1))),
							DeliveryUtil.getDoubleValueFromString(new DataFormatter().formatCellValue(row.getCell(2))),
							row.getCell(0).getStringCellValue());
				}
				if(null != discountCriteria) {
					DiscountCoupon coupon = new DiscountCoupon(row.getCell(0).getStringCellValue(), discountCriteria);
					discountCoupons.put(row.getCell(0).getStringCellValue(), coupon);
				}
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != workbook)
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return discountCoupons;
	}

	public static void main(String[] args) {

		System.out.println(readFromExcelFileAndLoadDiscounts());
	}
}
