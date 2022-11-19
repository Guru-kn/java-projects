package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Base64;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.example.demo.model.FeedbackStatus;
import com.example.demo.model.Student;

public class DemoClass {
	
	private static long get64LeastSignificantBitsForVersion1() {
	    Random random = new Random();
	    long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
	    long variant3BitFlag = 0x8000000000000000L;
	    return random63BitLong + variant3BitFlag;
	}

	private static long get64MostSignificantBitsForVersion1() {
	    LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
	    Duration duration = Duration.between(start, LocalDateTime.now());
	    long seconds = duration.getSeconds();
	    long nanos = duration.getNano();
	    long timeForUuidIn100Nanos = seconds * 10000000 + nanos * 100;
	    long least12SignificatBitOfTime = (timeForUuidIn100Nanos & 0x000000000000FFFFL) >> 4;
	    long version = 1 << 12;
	    return 
	      (timeForUuidIn100Nanos & 0xFFFFFFFFFFFF0000L) + version + least12SignificatBitOfTime;
	}
	
	public static void main(String[] args) {
		
		
		try {
			System.out.println(URLDecoder.decode("Lorem%20ipsum%20dolor%20sit%20amet,%20consectetuer%20adipiscin.PNG", "UTF-8"));
			System.out.println(URLDecoder.decode("Lorem ipsum dolor sit amet, consectetuer adipiscin.PNG", "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		  Set<Student> set = new HashSet<Student>();
//		  
//		  Student s1 = new Student(); s1.setStudentId("s1"); set.add(s1);
//		  
//		  Student s2 = new Student(); s2.setStudentId("s1"); set.add(s2);
//		  
//		  set.stream().forEach(s -> System.out.println(s.getStudentId())); //
		  // PatternMethod(4);
		  
		  // System.out.println("=========================================");
		  
//		  Set<String> setStr = new HashSet<String>(); setStr.add("s1");
//		  setStr.add("s1"); setStr.stream().forEach(s -> System.out.println(s));
		 
		
		// 213c010d-5b25-4050-b5d7-c0d11bf8c8df
		/*
		 * long most64SigBits = get64MostSignificantBitsForVersion1(); long
		 * least64SigBits = get64LeastSignificantBitsForVersion1(); UUID uuid = new
		 * UUID(most64SigBits, least64SigBits);
		 */
		
//		UUID uuid = UUID.randomUUID();
//		
//		System.out.println(uuid);
	}
	
//	static void PatternMethod(int n)
//	{
//	  for(int i=0;i<n;i++)
//	{
//	  System.out.println("");
//	  for(int j=0;j<=i;j++)
//		{
//		  System.out.print("*");
//		}
//	}
//	
//	}
}
