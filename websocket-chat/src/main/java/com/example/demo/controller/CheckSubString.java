package com.example.demo.controller;

public class CheckSubString {
	
	static boolean checkForSubString(String mainStr, String subStr){
		
		char[] mainStrArr = mainStr.toCharArray();
		char[] subStrArr = subStr.toCharArray();
		
		String strFormed = "";
		
		for(int i=0;i<mainStrArr.length;i++) {
			for(int j=0;j<subStrArr.length;j++) {
				if(subStrArr[j] == mainStrArr[i]) {
					strFormed += subStrArr[j];
					i++;
				} else if((mainStrArr.length - i) < subStrArr.length){ // when i pointer is ahead of substring length
					return false;
				}else {
					strFormed = "";
				}
			}
		}
		System.out.println("strFormed " + strFormed);
		if(strFormed.equalsIgnoreCase(subStr)) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(checkForSubString("CREATE", "EAT"));
	}
}
