package com.learning.springboot.dockerapp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//V\\Employee.  Manager
//001.             010
//002              010
//003              010
//010               020
//Report employees under a manager
//020 - 010,001,002,003
//010-001,002,003 
//
//
//


public class RaApp {
	
	public static Map<String, Set<String>> createMapping(Map<String, String> empManagerMapping){
		
		Map<String, Set<String>> empManagerMap =
				new HashMap<String, Set<String>>();
		
		Set<String> empList = null;
		
		for(Map.Entry<String, String> entry: empManagerMapping.entrySet()){
			
			if(null == empManagerMap.get(entry.getValue()) ||
					empManagerMap.size() == 0) {
				empList = new HashSet<String>();
				empList.add(entry.getKey());
			} else {
				empManagerMap.get(entry.getValue()).add(entry.getKey());
			}
			
			empManagerMap.put(entry.getValue(), empList);
		}
		
		return empManagerMap;
	}
	public RaApp() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		Map<String, String> empManagerMapping = new HashMap<String, String>();
		empManagerMapping.put("1", "10");
		empManagerMapping.put("2", "10");
		empManagerMapping.put("3", "10");
		empManagerMapping.put("10", "20");
		
		Map<String, Set<String>> finalEmpMgrMap = createMapping(empManagerMapping);
		
		
		System.out.println(getEmployeesByMangerId("20", finalEmpMgrMap));
	}
	
	public static Set<String> getEmployeesByMangerId(String managerId, 
			Map<String,  Set<String>> finalEmpMgrMap){
		
		
			Set<String> finalList = new HashSet<String>();
		
		Set<String> empList = null;
		
		empList = finalEmpMgrMap.get(managerId);
		
		if(null != empList && empList.size() > 0) {
			for(String emp: empList) {
				// finalList.add(emp);
				finalList.add(emp);
				System.out.println("emp " + emp);
				empList = finalEmpMgrMap.get(emp);
				if(empList != null && empList.size() > 0) {
					finalList.addAll(empList); //10
					for(String eachEmp: empList) {
						if(null !=finalEmpMgrMap.get(eachEmp)) {
							finalList.addAll(finalEmpMgrMap.get(eachEmp));//001, 002, 003
						}
					}
				}
			}
		}
		
		return finalList;
	}
}
