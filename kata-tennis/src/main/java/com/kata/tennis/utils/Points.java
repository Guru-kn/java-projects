package com.kata.tennis.utils;

import java.util.Random;

public class Points {
	
	public static Integer randomPointWinner() {
        return new Random().nextInt(2) + 1;
    }
	
	public static void main(String[] args) {
		System.out.println(Math.abs(1 - 2));
	}
}
