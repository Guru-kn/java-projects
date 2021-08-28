package com.learning.springboot.dockerapp;

import java.util.stream.Stream;

class MyAdd<T>{
	void add(T t) {
		
	}
}

public class MStanApp extends Thread{
	
	public static void main(String[] args) {
		Stream.of(1,2,3,4,5,6,7)
		.skip(5)
		.forEach(num->System.out.print(num + " "));
	}
}
